package com.edith.developmentassistant.interfaces.rest.dto.response.gitlab;

import com.edith.developmentassistant.infrastructure.external.gitlab.dto.GitBranch;
import java.util.List;

public record GitLabBranchesResponse(
        List<GitBranch> branches,
        int totalCount
) {
    public static GitLabBranchesResponse of(List<GitBranch> branches) {
        return new GitLabBranchesResponse(branches, branches.size());
    }
}