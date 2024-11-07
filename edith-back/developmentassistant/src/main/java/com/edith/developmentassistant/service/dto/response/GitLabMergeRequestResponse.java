package com.edith.developmentassistant.service.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GitLabMergeRequestResponse {
    private Long iid;
    private Author author;
    private List<Change> changes;

    @Data
    static public class Author {
        private String username;
    }

    @Data
    static public class Change {
        private String newPath;
        private String diff;
    }
}