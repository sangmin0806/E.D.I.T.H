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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        gitLabServiceClient.registerWebhook(request.id(), token);
    }

    public void commentCodeReview(WebhookEvent webhookEvent) {
        Long projectId = (long) webhookEvent.getProject().getId();
        Long mergeRequestIid = (long) webhookEvent.getObjectAttributes().getIid();

        Project project = findProjectById(projectId);

        try {
            MergeRequestDiffResponse mergeDiff = fetchMergeRequestDiff(projectId, mergeRequestIid, project.getToken());
            List<String> fixLogs = fetchFixLogs(projectId, project.getToken());
            String recentCommitMessage = fetchRecentCommitMessage(projectId, project.getToken());

            List<CodeReviewChanges> changes = mapChanges(mergeDiff.getChanges());
            List<String> mrSummaries = fetchRecentMRSummaries(projectId);

            String advice = fetchAdvice(projectId, project.getToken(), mrSummaries);

            CodeReviewResponse response = requestCodeReview(projectId, project.getToken(), mergeDiff, changes);

            saveMRSummary(webhookEvent, mergeRequestIid, response, project);

            updateDashboard(projectId.intValue(), response, recentCommitMessage, advice, fixLogs);

            postMergeRequestComment(projectId, mergeRequestIid, project.getToken(), response);
        } catch (Exception e) {
            log.error("Error occurred while processing webhook event", e);
        }
    }

    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    private MergeRequestDiffResponse fetchMergeRequestDiff(Long projectId, Long mergeRequestIid, String token) {
        return gitLabServiceClient.fetchMergeRequestDiff(projectId, mergeRequestIid, token);
    }

    private List<String> fetchFixLogs(Long projectId, String token) {
        return gitLabServiceClient.fetchFilteredCommitMessages(projectId, token);
    }

    private String fetchRecentCommitMessage(Long projectId, String token) {
        return gitLabServiceClient.fetchRecentCommitMessage(projectId, token);
    }

    private List<CodeReviewChanges> mapChanges(List<Change> changes) {
        return changes.stream()
                .map(Change::toCodeReviewChanges)
                .toList();
    }

    private List<String> fetchRecentMRSummaries(Long projectId) {
        return mrSummaryRepository.findTop10ByProjectIdOrderByCreatedDateDesc(projectId).stream()
                .map(MRSummary::getContent)
                .toList();
    }

    private String fetchAdvice(Long projectId, String token, List<String> mrSummaries) {
        return ragServiceClient.sendAdviceRequest(projectId, token, mrSummaries);
    }

    private CodeReviewResponse requestCodeReview(Long projectId, String token, MergeRequestDiffResponse mergeDiff,
                                                 List<CodeReviewChanges> changes) {
        CodeReviewRequest request = CodeReviewRequest.builder()
                .url("https://lab.ssafy.com")
                .projectId(projectId.toString())
                .branch(mergeDiff.getTargetBranch())
                .token(token)
                .changes(changes)
                .build();
        return ragServiceClient.commentCodeReview(request);
    }

    private void saveMRSummary(WebhookEvent webhookEvent, Long mergeRequestIid, CodeReviewResponse response,
                               Project project) {
        mrSummaryRepository.save(MRSummary.builder()
                .mrId(mergeRequestIid.toString())
                .gitlabEmail(webhookEvent.getUser().getEmail())
                .content(response.getReview())
                .project(project)
                .build());
    }

    private void updateDashboard(Integer projectId, CodeReviewResponse response, String recentCommitMessage,
                                 String advice, List<String> fixLogs) {
        String key = "dashboard:" + projectId;
        DashboardDto existingDashboard = (DashboardDto) redisTemplate.opsForValue().get(key);

        DashboardDto updatedDashboard = createOrUpdateDashboard(projectId, response, recentCommitMessage, advice,
                fixLogs, existingDashboard);

        redisTemplate.opsForValue().set(key, updatedDashboard);
    }

    private DashboardDto createOrUpdateDashboard(Integer projectId, CodeReviewResponse response,
                                                 String recentCommitMessage, String advice, List<String> fixLogs,
                                                 DashboardDto existingDashboard) {
        return DashboardDto.builder()
                .projectId(projectId)
                .recentCodeReview(defaultIfNullOrEmpty(response.getReview(), "No recent code review available"))
                .recentCommitMessage(defaultIfNullOrEmpty(recentCommitMessage, "No recent commit message available"))
                .advice(defaultIfNullOrEmpty(advice, "No advice provided"))
                .techStack(mergeTechStacks(existingDashboard == null ? List.of() : existingDashboard.techStack(),
                        response.getTechStack()))
                .fixLogs(defaultIfNullOrEmpty(fixLogs, List.of("No fix logs available")))
                .build();
    }

    private List<String> mergeTechStacks(List<String> existingTechStack, List<String> newTechStack) {
        Set<String> mergedSet = new HashSet<>();
        if (existingTechStack != null) {
            mergedSet.addAll(existingTechStack);
        }
        if (newTechStack != null) {
            mergedSet.addAll(newTechStack);
        }
        return new ArrayList<>(mergedSet);
    }

    private void postMergeRequestComment(Long projectId, Long mergeRequestIid, String token,
                                         CodeReviewResponse response) {
        gitLabServiceClient.addMergeRequestComment(projectId, mergeRequestIid, token, response.getReview(),
                response.getSummary());
    }
}
