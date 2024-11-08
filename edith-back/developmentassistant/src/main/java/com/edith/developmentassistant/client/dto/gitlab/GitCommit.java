package com.edith.developmentassistant.client.dto.gitlab;

import lombok.Getter;

import java.util.List;

@Getter
public class GitCommit {
    private String id;
    private String message;
    private String author_name;
    private String authored_date;
    private List<String> parent_ids;
}
