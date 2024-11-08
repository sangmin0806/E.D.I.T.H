package com.edith.developmentassistant.controller.dto.response.gitlab;

import com.edith.developmentassistant.client.dto.gitlab.GitCommit;

import java.util.List;

public record GitLabCommitsResponse (
      List<GitCommit> commits,
      int totalCommits
){
    public static GitLabCommitsResponse of(List<GitCommit> commits) {
        return new GitLabCommitsResponse(commits, commits.size());
    }
}
