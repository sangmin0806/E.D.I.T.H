package com.edith.developmentassistant.client.dto.gitlab;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GitBranch {
    private String name;
    private GitCommit commit;
    private boolean merged;
}