package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.user.UserServiceClient;
import com.edith.developmentassistant.repository.MRSummaryRepository;
import com.edith.developmentassistant.repository.PortfolioRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.MergeRequest;
import com.edith.developmentassistant.service.dto.Summary;
import com.edith.developmentassistant.service.dto.request.CreatePortfolioServiceRequest;
import com.edith.developmentassistant.service.dto.response.GitLabMergeRequestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
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

    // Portfolio 생성 로직
    public String createPortfolio(String accessToken, String projectId, String branch) {
        // 1. User 찾기
//        UserDto user = userServiceClient.getUserByToken(accessToken);

        // 2. project summery 찾기 -> projectId 로 찾기
        List<Summary> summaries = mrSummaryRepository.findByProjectId(Long.parseLong(projectId)).stream()
                .map(Summary::from)
                .toList();

        // 3. GitLab 에서 해당 Branch 의 MR 리스트 받아 파싱하기 (WebClient)
        List<MergeRequest> mergeRequests = getMergedMRs(projectId, branch, "NHMeAABxUvZVyLq6u5Qx");

        // 4. Flask 에 포폴 생성 요청하기
        CreatePortfolioServiceRequest request = new CreatePortfolioServiceRequest(
                "doublehyun98@gmail.com",
                summaries,
                mergeRequests
        );
        log.info("Create portfolio request: {}", request);
        // 5. 포트폴리오 받아 저장, 반환하기

        return "성공 ㅇㅇ";
    }

    public List<MergeRequest> getMergedMRs(String projectId, String branch, String accessToken) {
        return gitLabWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/projects/{projectId}/merge_requests")
                        .queryParam("target_branch", branch)
                        .queryParam("state", "merged")
                        .build(projectId))
                .header("PRIVATE-TOKEN", accessToken)  // Bearer 토큰 대신 PRIVATE-TOKEN 사용
                .retrieve()
                .bodyToFlux(GitLabMergeRequestResponse.class)
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
                .onErrorResume(e -> {
                    log.error("Error fetching merge requests: ", e);
                    return Mono.just(Collections.emptyList());
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
}


