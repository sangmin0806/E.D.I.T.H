package com.edith.developmentassistant.interfaces.rest.dto.response.gitlab;

import com.edith.developmentassistant.infrastructure.external.gitlab.dto.GitCommit;

import java.util.List;

public record GitLabCommitsResponse (
      List<GitCommit> commits,
      int totalCommits
){
    public static GitLabCommitsResponse of(List<GitCommit> commits) {
        return new GitLabCommitsResponse(commits, commits.size());
    }
}
