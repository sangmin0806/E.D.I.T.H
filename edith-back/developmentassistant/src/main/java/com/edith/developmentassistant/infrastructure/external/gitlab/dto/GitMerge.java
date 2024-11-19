package com.edith.developmentassistant.infrastructure.external.gitlab.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GitMerge {
    private String source_branch;
    private String target_branch;
    private String merge_commit_sha;
    private Long iid;

    private Author author; // 작성자 정보 추가

    @Getter
    @Builder
    public static class Author {
        private String name;
        private String email;
    }
}
