package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;

import lombok.Getter;

@Getter
public class LastCommit extends BaseWebhook {

    private String id;
    private String message;
    private String title;
    private String timestamp;
    private String url;
    private CommitAuthor author;
}
