package com.edith.developmentassistant.controller;

import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.controller.dto.response.webhook.WebhookEvent;
import com.edith.developmentassistant.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;


    @PostMapping
    public void getEventFromWebhook(@RequestBody WebhookEvent webhookEvent) {
        log.info("Received webhookEvent: {}", webhookEvent);
        webhookService.commentCodeReview(webhookEvent);
    }
}
