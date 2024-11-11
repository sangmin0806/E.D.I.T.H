package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.user.UserServiceClient;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.repository.MRSummaryRepository;
import com.edith.developmentassistant.repository.PortfolioRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.MergeRequest;
import com.edith.developmentassistant.service.dto.Summary;
import com.edith.developmentassistant.service.dto.request.CreatePortfolioServiceRequest;
import com.edith.developmentassistant.service.dto.response.CreatePortfolioResponse;
import com.edith.developmentassistant.service.dto.response.GitLabMergeRequestResponse;
import com.edith.developmentassistant.service.dto.response.FlaskPortfolioResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserProjectRepository userProjectRepository;
    private final MRSummaryRepository mrSummaryRepository;
    private final UserServiceClient userServiceClient;
    private final WebClient gitLabWebClient;
    private final RestTemplate portfolioRestTemplate;
    private final ProjectService projectService;

    @Value("${api.flask.portfolio}")
    String FLASK_PORTFOLIO_URL;

    // Portfolio 생성 로직
    public CreatePortfolioResponse createPortfolio(String accessToken, String projectId, String branch) {

        try {
            // 1. User, userProject 찾기
//            UserDto user = userServiceClient.getUserByToken(accessToken);
            UserDto user = createUserDto();
            UserProject userProject = projectService.findUserProjectByUserIdAndProjectId(user.getId(),
                    Long.parseLong(projectId));

            // 2. project summery 찾기 -> id 로 찾기
            List<Summary> summaries = mrSummaryRepository.findByProjectId(Long.parseLong(projectId)).stream()
                    .map(Summary::from)
                    .toList();

            // 3. GitLab 에서 해당 Branch 의 MR 리스트 받아 파싱하기 (WebClient)
            MergeRequestDateRange mergeRequestdateRange = getMergedMRs(projectId, branch, "NHMeAABxUvZVyLq6u5Qx");

            // 4. Flask 에 포폴 생성 요청하기
            CreatePortfolioServiceRequest request = new CreatePortfolioServiceRequest(
                    "doublehyun98@gmail.com",
                    summaries,
                    mergeRequestdateRange.getMergeRequests()
            );
//            log.info("Create portfolio request: {}", request);
            // 5. 포트폴리오 받아 저장, 반환하기
            ResponseEntity<FlaskPortfolioResponse> response = portfolioRestTemplate.postForEntity(
                    FLASK_PORTFOLIO_URL,
                    request,
                    FlaskPortfolioResponse.class // 응답 타입
            );

            return CreatePortfolioResponse.builder()
                    .portfolio(response.getBody().getPortfolio())
//                    .name(userProject.getTitle())
//                    .content(userProject.getDescription())
                    .endDate(mergeRequestdateRange.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .startDate(mergeRequestdateRange.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();

//            return response.getBody().getPortfolio();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MergeRequestDateRange getMergedMRs(String projectId, String branch, String accessToken) {
        return gitLabWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/projects/{id}/merge_requests")
                        .queryParam("target_branch", branch)
                        .queryParam("state", "merged")
                        .build(projectId))
                .header("PRIVATE-TOKEN", accessToken)
                .retrieve()
                .bodyToFlux(GitLabMergeRequestResponse.class)
                .collectList()
                .flatMap(mergeRequests -> {
                    LocalDateTime firstPreparedAt = mergeRequests.stream()
                            .map(GitLabMergeRequestResponse::getPreparedAt)
                            .filter(Objects::nonNull)
                            .min(LocalDateTime::compareTo)
                            .orElse(null);

                    LocalDateTime lastMergedAt = mergeRequests.stream()
                            .map(GitLabMergeRequestResponse::getMergedAt)
                            .filter(Objects::nonNull)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    return Flux.fromIterable(mergeRequests)
                            .parallel()
                            .runOn(Schedulers.boundedElastic())
                            .flatMap(mr -> getMRDiff(projectId, mr.getIid(), accessToken)
                                    .map(diff -> new MergeRequest(
                                            String.valueOf(mr.getIid()),
                                            mr.getAuthor() != null ? mr.getAuthor().getUsername() : "",
                                            Optional.ofNullable(mr.getChanges())
                                                    .orElse(Collections.emptyList())
                                                    .stream()
                                                    .map(GitLabMergeRequestResponse.Change::getNewPath)
                                                    .collect(Collectors.joining(",")),
                                            diff
                                    )))
                            .sequential()
                            .collectList()
                            .map(mrs -> new MergeRequestDateRange(firstPreparedAt, lastMergedAt, mrs));
                })
                .onErrorResume(e -> {
                    log.error("Error fetching merge requests: ", e);
                    return Mono.just(new MergeRequestDateRange(null, null, Collections.emptyList()));
                })
                .block(Duration.ofSeconds(30));
    }

    private Mono<String> getMRDiff(String projectId, Long mrIid, String accessToken) {
        return gitLabWebClient
                .get()
                .uri("/projects/{projectId}/merge_requests/{mrIid}/changes", projectId, mrIid)
                .header("PRIVATE-TOKEN", accessToken)
                .retrieve()
                .bodyToMono(GitLabMergeRequestResponse.class)
                .map(mr -> Optional.ofNullable(mr.getChanges())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(change -> Optional.ofNullable(change.getDiff()).orElse(""))
                        .collect(Collectors.joining("\n")))
                .onErrorResume(e -> {
                    log.error("Error fetching MR diff: ", e);
                    return Mono.just("");
                })
                .timeout(Duration.ofSeconds(10));
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .id(1L)
                .email("doublehyun98")
                .password("1234")
                .vcsBaseUrl("https://lab.ssafy.com/")
                .vcsAccessToken("ZH3_Ft1HJmHqwXYmgYHs")
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    class MergeRequestDateRange {
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;
        private final List<MergeRequest> mergeRequests;
    }
}


