package com.edith.developmentassistant.controller.dto.response.webhook;

import lombok.Getter;

@Getter
public class webhookRepository extends BaseWebhook {

    private String name;
    private String url;
    private String description;
    private String homepage;
}
