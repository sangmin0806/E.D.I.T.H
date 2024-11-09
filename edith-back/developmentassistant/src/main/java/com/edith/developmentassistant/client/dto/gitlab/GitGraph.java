package com.edith.developmentassistant.client.dto.gitlab;
import java.util.List;

public record GitGraph(String sourceBranch, String targetBranch, GitCommit mergeCommit, List<GitCommit> sourceBranchCommits) {
    public static GitGraph of(String sourceBranch, String targetBranch, GitCommit mergeCommit, List<GitCommit> sourceBranchCommits) {
        return new GitGraph(sourceBranch, targetBranch, mergeCommit, sourceBranchCommits);
    }
}