package com.edith.developmentassistant.infrastructure.external.gitlab.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GitBranch {
    private String name;
    private GitCommit commit;
    private boolean merged;
}