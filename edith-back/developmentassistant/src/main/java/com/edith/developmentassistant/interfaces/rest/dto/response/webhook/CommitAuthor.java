package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;

import lombok.Getter;

@Getter
public class CommitAuthor extends BaseWebhook {

    private String name;
    private String email;
}
