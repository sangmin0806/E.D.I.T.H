package com.edith.developmentassistant.controller.dto.response.webhook;

import lombok.Getter;

@Getter
public class CommitAuthor extends BaseWebhook {

    private String name;
    private String email;
}
