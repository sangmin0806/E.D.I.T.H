package com.edith.developmentassistant.controller.dto.response.webhook;


import lombok.Getter;

@Getter
public class webhookUser extends BaseWebhook {

    private int id;
    private String name;
    private String username;
    private String avatarUrl;
    private String email;
}
