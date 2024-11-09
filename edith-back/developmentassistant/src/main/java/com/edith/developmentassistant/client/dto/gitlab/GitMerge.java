package com.edith.developmentassistant.client.dto.gitlab;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GitMerge {
    private String source_branch;
    private String target_branch;
    private String merge_commit_sha;
    private Long iid;
}
