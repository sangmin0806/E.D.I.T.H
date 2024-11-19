package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;

import lombok.Getter;

@Getter
public class WebhookRepository extends BaseWebhook {

    private String name;
    private String url;
    private String description;
    private String homepage;
}
