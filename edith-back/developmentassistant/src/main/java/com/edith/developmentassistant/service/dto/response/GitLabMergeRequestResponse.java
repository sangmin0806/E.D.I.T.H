package com.edith.developmentassistant.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GitLabMergeRequestResponse {
    private Long iid;
    private Author author;
    private List<Change> changes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("prepared_at")
    private LocalDateTime preparedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("merged_at")
    private LocalDateTime mergedAt;

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