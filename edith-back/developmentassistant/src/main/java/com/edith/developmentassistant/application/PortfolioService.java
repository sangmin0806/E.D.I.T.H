package com.edith.developmentassistant.application;

import com.edith.developmentassistant.domain.model.Project;
import com.edith.developmentassistant.infrastructure.dto.UserDto;
import com.edith.developmentassistant.infrastructure.client.user.UserServiceClient;
import com.edith.developmentassistant.domain.model.Portfolio;
import com.edith.developmentassistant.domain.model.UserProject;
import com.edith.developmentassistant.infrastructure.repository.jpa.MRSummaryRepository;
import com.edith.developmentassistant.infrastructure.repository.jpa.PortfolioRepository;
import com.edith.developmentassistant.infrastructure.repository.jpa.UserProjectRepository;
import com.edith.developmentassistant.application.dto.MergeRequest;
import com.edith.developmentassistant.application.dto.MergeRequestDateRange;
import com.edith.developmentassistant.application.dto.PortfolioDto;
import com.edith.developmentassistant.application.dto.Summary;
import com.edith.developmentassistant.application.dto.request.CreatePortfolioServiceRequest;
import com.edith.developmentassistant.application.dto.response.FindAllPortfolioResponse;
import com.edith.developmentassistant.application.dto.response.FlaskPortfolioResponse;
import com.edith.developmentassistant.application.dto.response.GitLabMergeRequestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public PortfolioDto createPortfolio(String accessToken, String projectId, String branch) {

        try {

//            UserDto user = userServiceClient.getUserByToken(accessToken);
//
//            UserProject userProject = projectService.findUserProjectByUserIdAndProjectId(user.getUserId(),
//                    Long.parseLong(projectId));

            UserDto user = createUser();
            UserProject userProject = createProject();

            if (userProject == null) {
                log.error("PortfolioService -> UserProject not found");
                throw new RuntimeException("PortfolioService -> UserProject not found");
            }


            List<Summary> summaries = mrSummaryRepository.findByProjectId(Long.parseLong(projectId)).stream()
                    .map(Summary::from)
                    .toList();


            // 3. GitLab 에서 해당 Branch 의 MR 리스트 받아 파싱하기 (WebClient)
            MergeRequestDateRange mergeRequestdateRange = getMergedMRs(projectId, branch, user.getVcsAccessToken());
            log.info("Merged MRs from {} to {}", mergeRequestdateRange.getStartDate(), mergeRequestdateRange.getEndDate());

            // 4. Flask 에 포폴 생성 요청하기
            CreatePortfolioServiceRequest request = new CreatePortfolioServiceRequest(
                    user.getEmail(),
                    userProject.getDescription(),
                    summaries,
                    mergeRequestdateRange.getMergeRequests()
            );

//            log.info("Flask URL: {}", FLASK_PORTFOLIO_URL);
//            log.info("Request data: {}", request);

            // 5. 포트폴리오 받아 반환하기
            ResponseEntity<FlaskPortfolioResponse> response = portfolioRestTemplate.postForEntity(
                    FLASK_PORTFOLIO_URL,
                    request,
                    FlaskPortfolioResponse.class // 응답 타입
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Flask 서버 오류: 상태코드 {}", response.getStatusCode());
                throw new RuntimeException("Flask 서버 응답 오류");
            }

            return PortfolioDto.builder()
                    .portfolio(response.getBody().getPortfolio())
                    .projectId(Long.parseLong(projectId))
                    .name(userProject.getTitle())
                    .content(userProject.getDescription())
                    .endDate(mergeRequestdateRange.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .startDate(mergeRequestdateRange.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MergeRequestDateRange getMergedMRs(String projectId, String branch, String accessToken) {
        int perPage = 100; // GitLab 최대 값
        List<GitLabMergeRequestResponse> allMergeRequests = new ArrayList<>();

        try {
            int currentPage = 1;
            int totalPages;

            do {
                // 페이지별 요청
                int finalCurrentPage = currentPage;
                ResponseEntity<List<GitLabMergeRequestResponse>> response = gitLabWebClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/projects/{id}/merge_requests")
//                                .queryParam("target_branch", branch)
                                .queryParam("state", "merged")
                                .queryParam("per_page", perPage)
                                .queryParam("page", finalCurrentPage)
                                .build(projectId))
                        .header("PRIVATE-TOKEN", accessToken)
                        .retrieve()
                        .toEntityList(GitLabMergeRequestResponse.class)
                        .block(Duration.ofSeconds(30));

                // 응답 데이터 추가
                if (response != null && response.getBody() != null) {
                    allMergeRequests.addAll(response.getBody());
                }

                // 총 페이지 수 추출
                HttpHeaders headers = response.getHeaders();
                totalPages = Integer.parseInt(headers.getFirst("X-Total-Pages"));

                currentPage++; // 다음 페이지로 이동
            } while (currentPage <= totalPages);

            // 필요한 데이터 추출 및 반환
            LocalDateTime firstPreparedAt = allMergeRequests.stream()
                    .map(GitLabMergeRequestResponse::getPreparedAt)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime lastMergedAt = allMergeRequests.stream()
                    .map(GitLabMergeRequestResponse::getMergedAt)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            // MR Diff 데이터 추가 병렬 처리
            List<MergeRequest> mrs = Flux.fromIterable(allMergeRequests)
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
                    .block();


//            log.info("마지막={}", currentPage);
            return new MergeRequestDateRange(firstPreparedAt, lastMergedAt, mrs);

        } catch (Exception e) {
            log.error("Error fetching merge requests: ", e);
            return new MergeRequestDateRange(null, null, Collections.emptyList());
        }
    }

    public PortfolioDto savePortfolio(String accessToken, PortfolioDto portfolio) {

        UserDto user = userServiceClient.getUserByToken(accessToken);
        UserProject userProject = projectService.findUserProjectByUserIdAndProjectId(user.getUserId(),
                portfolio.getProjectId());

        Optional<Portfolio> existingPortfolio = portfolioRepository.findByUserProject(userProject);
        PortfolioDto savedPortfolio;
        if (existingPortfolio.isPresent()) {
            // 기존 portfolio 업데이트
            Portfolio portfolioToUpdate = existingPortfolio.get();
            portfolioToUpdate.updateContent(portfolio.getPortfolio());  // 내용 업데이트 메소드 필요
            portfolioToUpdate.updateDates(  // 날짜 업데이트 메소드 필요
                    LocalDate.parse(portfolio.getStartDate()).atStartOfDay(),
                    LocalDate.parse(portfolio.getEndDate()).atStartOfDay()
            );
            savedPortfolio = new PortfolioDto(userProject, portfolioRepository.save(portfolioToUpdate));
        } else {
            // 새로운 portfolio 생성
            Portfolio savePortfolio = Portfolio.builder()
                    .content(portfolio.getPortfolio())
                    .userProject(userProject)
                    .startDate(LocalDate.parse(portfolio.getStartDate()).atStartOfDay())
                    .endDate(LocalDate.parse(portfolio.getEndDate()).atStartOfDay())
                    .build();
            savedPortfolio = new PortfolioDto(userProject, portfolioRepository.save(savePortfolio));
        }

        return savedPortfolio;
    }

    public List<FindAllPortfolioResponse> findAllPortfolioResponseList(String accessToken) {
        UserDto user = userServiceClient.getUserByToken(accessToken);
        return portfolioRepository.findAllDtoByUserId(user.getUserId());
    }

    public PortfolioDto getPortfolio(String accessToken, String portfolioId) {

        UserDto user = userServiceClient.getUserByToken(accessToken);
        UserProject userProject = userProjectRepository.findByUserIdAndProjectId(user.getUserId(),
                        Long.parseLong(portfolioId))
                .orElse(null);

        Portfolio portfolio = portfolioRepository.findByUserProject(userProject)
                .orElse(null);

        if (portfolio == null || userProject == null) {
            return null;
        }
        return new PortfolioDto(userProject, portfolio);
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

    private UserDto createUser() {
        return UserDto.builder()
                .userId(10L)
                .vcsBaseUrl("https://lab.ssafy.com")
                .vcsAccessToken("oFToGMmLxsGCEyR6741s")
                .email("Lee-JoungHyun")
                .password("1234")
                .build();
    }

    private UserProject createProject() {
        Project project = Project.builder()
                .projectId(824085L)
                .url("https://lab.ssafy.com")
                .build();

        return UserProject.builder()
                .title("E.D.I,T.H.")
                .description("RAG 를 활용한 AI 코드리뷰, 포트폴리오 생성 서비스")
                .project(project)
                .build();
    }

}


