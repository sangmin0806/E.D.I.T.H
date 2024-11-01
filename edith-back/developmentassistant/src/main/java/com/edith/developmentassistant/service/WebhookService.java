package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.GitLabClient;
import com.edith.developmentassistant.service.dto.request.RegisterRepositoryServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final GitLabClient gitLabClient;

    public void registerWebhook(RegisterRepositoryServiceRequest createWebhookServiceRequest) {
        String branch = createWebhookServiceRequest.pushEventsBranchFilter();
        Long projectId = createWebhookServiceRequest.projectId();
        String personalAccessToken = createWebhookServiceRequest.personalAccessToken();
        gitLabClient.registerWebhook(branch, projectId, personalAccessToken);
    }
}
