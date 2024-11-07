package com.edith.developmentassistant.controller.dto.response.webhook;

import java.util.List;
import lombok.Getter;

@Getter
public class WebhookEvent extends BaseWebhook {

    private String objectKind;
    private String eventType;
    private webhookUser user;
    private webhookProject project;
    private ObjectAttributes objectAttributes;
    private List<Object> labels;
    private Changes changes;
    private webhookRepository repository;

}
