package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.GitLabClient;
import com.edith.developmentassistant.service.dto.request.CreateWebhookServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final GitLabClient gitLabClient;

    public void registerWebhook(CreateWebhookServiceRequest createWebhookServiceRequest) {
        String branch = createWebhookServiceRequest.pushEventsBranchFilter();
        Integer projectId = createWebhookServiceRequest.projectId();
        String personalAccessToken = createWebhookServiceRequest.personalAccessToken();
        gitLabClient.registerWebhook(branch, projectId, personalAccessToken);
    }
}
