package com.edith.developmentassistant.controller;

import com.edith.developmentassistant.controller.dto.request.CreateWebhookRequest;
import com.edith.developmentassistant.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public void createWebhook(@RequestBody CreateWebhookRequest createWebhookRequest) {
        webhookService.registerWebhook(createWebhookRequest.toCreateWebhookServiceRequest());
    }
}
