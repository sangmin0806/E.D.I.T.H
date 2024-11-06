package com.edith.developmentassistant.client.dto.mergerequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MergeRequestDiffResponse {

    private int id;
    private int iid;
    private int projectId;
    private String title;
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;
    private MRUser mergedBy;
    private MRUser mergeUser;
    private String mergedAt;
    private String targetBranch;
    private String sourceBranch;
    private int userNotesCount;
    private int upvotes;
    private int downvotes;
    private MRUser author;
    private List<MRUser> assignees;
    private MRUser assignee;
    private List<MRUser> reviewers;
    private int sourceProjectId;
    private int targetProjectId;
    private boolean draft;
    private boolean imported;
    private String importedFrom;
    private boolean workInProgress;
    private boolean mergeWhenPipelineSucceeds;
    private String mergeStatus;
    private String detailedMergeStatus;
    private String sha;
    private String mergeCommitSha;
    private String squashCommitSha;
    private boolean shouldRemoveSourceBranch;
    private boolean forceRemoveSourceBranch;
    private String preparedAt;
    private String reference;
    private References references;
    private String webUrl;
    private TimeStats timeStats;
    private boolean squash;
    private boolean squashOnMerge;
    private TaskCompletionStatus taskCompletionStatus;
    private boolean hasConflicts;
    private boolean blockingDiscussionsResolved;
    private boolean subscribed;
    private String changesCount;
    private DiffRefs diffRefs;
    private List<Change> changes;
    private boolean overflow;
    private UserPermissions user;
}

