package com.edith.developmentassistant.infrastructure.external.gitlab.dto;
import java.util.List;

public record GitGraph(String sourceBranch, String targetBranch, GitCommit mergeCommit, List<GitCommit> sourceBranchCommits) {
    public static GitGraph of(String sourceBranch, String targetBranch, GitCommit mergeCommit, List<GitCommit> sourceBranchCommits) {
        return new GitGraph(sourceBranch, targetBranch, mergeCommit, sourceBranchCommits);
    }
}