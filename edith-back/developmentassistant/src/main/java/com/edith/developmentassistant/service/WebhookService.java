package com.edith.developmentassistant.service;

import static com.edith.developmentassistant.utils.StringUtils.defaultIfNullOrEmpty;

import com.edith.developmentassistant.client.dto.mergerequest.Change;
import com.edith.developmentassistant.client.dto.mergerequest.MergeRequestDiffResponse;
import com.edith.developmentassistant.client.dto.rag.CodeReviewChanges;
import com.edith.developmentassistant.client.dto.rag.CodeReviewRequest;
import com.edith.developmentassistant.client.dto.rag.CodeReviewResponse;
import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.client.rag.RagServiceClient;
import com.edith.developmentassistant.controller.dto.response.webhook.WebhookEvent;
import com.edith.developmentassistant.domain.MRSummary;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.repository.MRSummaryRepository;
import com.edith.developmentassistant.repository.ProjectRepository;
import com.edith.developmentassistant.service.dto.DashboardDto;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final GitLabServiceClient gitLabServiceClient;
    private final ProjectRepository projectRepository;
    private final RagServiceClient ragServiceClient;
    private final MRSummaryRepository mrSummaryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void registerWebhook(RegisterProjectServiceRequest request, String token) {
        Long projectId = request.id();
        gitLabServiceClient.registerWebhook(projectId, token);
    }

    public void commentCodeReview(WebhookEvent webhookEvent) {
        Long projectId = (long) webhookEvent.getProject().getId();
        Long mergeRequestIid = (long) webhookEvent.getObjectAttributes().getIid();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        String token = project.getToken();

        MergeRequestDiffResponse MergeDiff = gitLabServiceClient.fetchMergeRequestDiff(projectId, mergeRequestIid,
                token);

        String recentCommitMessage = gitLabServiceClient.fetchRecentCommitMessage(projectId, token);
        List<String> fixLogs = gitLabServiceClient.fetchFilteredCommitMessages(projectId, token);
        String baseUrl = "https://lab.ssafy.com";
        List<CodeReviewChanges> changes = MergeDiff.getChanges().stream().map(Change::toCodeReviewChanges)
                .toList();

        CodeReviewRequest request = CodeReviewRequest.builder()
                .url(baseUrl)
                .projectId(projectId.toString())
                .branch(MergeDiff.getTargetBranch())
                .token(token)
                .changes(changes).build();

        log.info("MergeRequestDiffResponse: {}", MergeDiff.getChanges());

        CodeReviewResponse codeReviewResponse = ragServiceClient.commentCodeReview(request);

        saveMRSummary(webhookEvent, mergeRequestIid, codeReviewResponse, project);

        saveDashboardDto(projectId.intValue(), codeReviewResponse.getReview(), recentCommitMessage,
                codeReviewResponse.getSummary(), codeReviewResponse.getTechStack(), fixLogs);

        gitLabServiceClient.addMergeRequestComment(projectId, mergeRequestIid, token, codeReviewResponse.getReview(),
                codeReviewResponse.getSummary());
    }

    private void saveDashboardDto(Integer projectId, String recentCodeReview, String recentCommitMessage, String advice,
                                  List<String> techStack, List<String> fixLogs) {
        String key = "dashboard:" + projectId;

        // Redis에서 기존 데이터를 가져오기
        DashboardDto existingDashboard = (DashboardDto) redisTemplate.opsForValue().get(key);

        DashboardDto dashboardDto = getDashboardDto(
                projectId,
                recentCodeReview,
                recentCommitMessage,
                advice,
                techStack,
                fixLogs,
                existingDashboard
        );

        // Redis에 업데이트된 데이터 저장
        redisTemplate.opsForValue().set(key, dashboardDto);
    }

    private DashboardDto getDashboardDto(Integer projectId,
                                         String recentCodeReview,
                                         String recentCommitMessage,
                                         String advice,
                                         List<String> techStack,
                                         List<String> fixLogs,
                                         DashboardDto existingDashboard) {
        DashboardDto dashboardDto;
        if (existingDashboard != null) {
            // 기존 데이터를 가져오고 techStack만 유지
            dashboardDto = updateAndOverwriteDashboardDto(
                    projectId,
                    recentCodeReview,
                    recentCommitMessage,
                    advice,
                    existingDashboard.techStack(),
                    fixLogs);
        } else {
            // 기존 데이터가 없으면 새로 생성
            dashboardDto = createDashboardDto(projectId, recentCodeReview, recentCommitMessage, advice, techStack,
                    fixLogs);
        }
        return dashboardDto;
    }

    private DashboardDto updateAndOverwriteDashboardDto(Integer projectId,
                                                        String recentCodeReview,
                                                        String recentCommitMessage,
                                                        String advice,
                                                        List<String> techStack,
                                                        List<String> fixLogs) {
        return DashboardDto.builder()
                .projectId(projectId)
                .recentCodeReview(defaultIfNullOrEmpty(recentCodeReview, "No recent code review available"))
                .recentCommitMessage(defaultIfNullOrEmpty(recentCommitMessage, "No recent commit message available"))
                .advice(defaultIfNullOrEmpty(advice, "No advice provided"))
                .techStack(techStack) // 기존 techStack 유지
                .fixLogs(defaultIfNullOrEmpty(fixLogs, List.of("No fix logs available")))
                .build();
    }

    private DashboardDto createDashboardDto(Integer projectId, String recentCodeReview, String recentCommitMessage,
                                            String advice, List<String> techStack, List<String> fixLogs) {
        return DashboardDto.builder()
                .projectId(projectId)
                .recentCodeReview(defaultIfNullOrEmpty(recentCodeReview, "No recent code review available"))
                .recentCommitMessage(defaultIfNullOrEmpty(recentCommitMessage, "No recent commit message available"))
                .advice(defaultIfNullOrEmpty(advice, "No advice provided"))
                .techStack(defaultIfNullOrEmpty(techStack, List.of("Default Tech Stack")))
                .fixLogs(defaultIfNullOrEmpty(fixLogs, List.of("No fix logs available")))
                .build();
    }

    private void saveMRSummary(WebhookEvent webhookEvent, Long mergeRequestIid, CodeReviewResponse codeReviewResponse,
                               Project project) {
        mrSummaryRepository.save(MRSummary.builder()
                .mrId(mergeRequestIid.toString())
                .gitlabEmail(webhookEvent.getUser().getEmail())
                .content(codeReviewResponse.getReview())
                .project(project)
                .build());
    }
}
