package com.edith.developmentassistant.controller.dto.response.gitlab;

import com.edith.developmentassistant.client.dto.gitlab.GitBranch;
import java.util.List;

public record GitLabBranchesResponse(
        List<GitBranch> branches,
        int totalCount
) {
    public static GitLabBranchesResponse of(List<GitBranch> branches) {
        return new GitLabBranchesResponse(branches, branches.size());
    }
}