package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final GitLabServiceClient gitLabServiceClient;

    public void registerWebhook(RegisterProjectServiceRequest request , String token) {
        Long projectId = request.projectId();
        gitLabServiceClient.registerWebhook(projectId, token);
    }
}
