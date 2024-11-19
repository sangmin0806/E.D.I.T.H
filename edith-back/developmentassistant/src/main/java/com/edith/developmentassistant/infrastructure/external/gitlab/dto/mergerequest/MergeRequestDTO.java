package com.edith.developmentassistant.infrastructure.external.gitlab.dto.mergerequest;

import java.time.LocalDateTime;
import java.util.List;

public class MergeRequestDTO {
    private int id;
    private int iid;
    private int projectId;
    private String title;
    private String description;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User mergedBy;
    private User mergeUser;
    private LocalDateTime mergedAt;
    private String targetBranch;
    private String sourceBranch;
    private int userNotesCount;
    private int upvotes;
    private int downvotes;
    private User author;
    private List<User> assignees;
    private User assignee;
    private List<User> reviewers;
    private int sourceProjectId;
    private int targetProjectId;
    private boolean workInProgress;
    private String mergeStatus;
    private String detailedMergeStatus;
    private String sha;
    private String mergeCommitSha;
    private String webUrl;
    private TimeStats timeStats;
    private TaskCompletionStatus taskCompletionStatus;

    // Getters and Setters for each field

    public static class User {
        private int id;
        private String username;
        private String name;
        private String state;
        private String avatarUrl;
        private String webUrl;

    }

    public static class TimeStats {
        private int timeEstimate;
        private int totalTimeSpent;
    }

    public static class TaskCompletionStatus {
        private int count;
        private int completedCount;
    }
}

