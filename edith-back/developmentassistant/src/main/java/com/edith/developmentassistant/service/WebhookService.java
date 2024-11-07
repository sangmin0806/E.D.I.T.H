package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.mergerequest.Change;
import com.edith.developmentassistant.client.dto.mergerequest.MergeRequestDiffResponse;
import com.edith.developmentassistant.client.dto.rag.CodeReviewChanges;
import com.edith.developmentassistant.client.dto.rag.CodeReviewRequest;
import com.edith.developmentassistant.client.dto.rag.CodeReviewResponse;
import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.client.rag.RagServiceClient;
import com.edith.developmentassistant.controller.dto.response.webhook.WebhookEvent;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.repository.ProjectRepository;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final GitLabServiceClient gitLabServiceClient;
    private final ProjectRepository projectRepository;
    private final RagServiceClient ragServiceClient;

    public void registerWebhook(RegisterProjectServiceRequest request, String token) {
        Long projectId = request.projectId();
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
        MergeDiff.getProjectId();
        URL urlObj = null;
        try {
            urlObj = new URL(MergeDiff.getWebUrl());
        } catch (Exception e) {
            log.error("URL is not valid");
        }

        String baseUrl = urlObj.getProtocol() + "://" + urlObj.getHost() + "/";
        List<CodeReviewChanges> changes = MergeDiff.getChanges().stream().map(Change::toCodeReviewChanges)
                .toList();

        CodeReviewRequest request = CodeReviewRequest.builder()
                .branch(baseUrl)
                .projectId(projectId.toString())
                .branch(MergeDiff.getTargetBranch())
                .token(token)
                .changes(changes).build();

        log.info("MergeRequestDiffResponse: {}", MergeDiff.getChanges());

        CodeReviewResponse codeReviewResponse = ragServiceClient.commentCodeReview(request);

        // TODO : DB에 저장하기
        codeReviewResponse.getSummary();

        gitLabServiceClient.addMergeRequestComment(projectId, mergeRequestIid, token, codeReviewResponse.getReview());
    }
}
