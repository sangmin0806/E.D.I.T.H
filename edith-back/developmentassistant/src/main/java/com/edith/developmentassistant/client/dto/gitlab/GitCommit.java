package com.edith.developmentassistant.client.dto.gitlab;

import lombok.Getter;

@Getter
public class GitCommit {
    private String id;
    private String message;
    private String authorName;
    private String authoredDate;
}
