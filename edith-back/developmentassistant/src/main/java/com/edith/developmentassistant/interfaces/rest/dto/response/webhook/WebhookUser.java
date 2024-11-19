package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;


import lombok.Getter;

@Getter
public class WebhookUser extends BaseWebhook {

    private int id;
    private String name;
    private String username;
    private String avatarUrl;
    private String email;
}
