package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookEvent extends BaseWebhook {

    private String objectKind;
    private String eventType;
    private WebhookUser user;
    private WebhookProject project;
    private ObjectAttributes objectAttributes;
    private List<Object> labels;
    private Changes changes;
    private WebhookRepository repository;
}

