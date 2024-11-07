package com.edith.developmentassistant.controller.dto.response.webhook;

import com.edith.developmentassistant.domain.Project;
import java.util.List;
import lombok.Getter;

@Getter
public class ObjectAttributes extends BaseWebhook {

    private Integer assigneeId;
    private Integer authorId;
    private String createdAt;
    private String description;
    private boolean draft;
    private Integer headPipelineId;
    private int id;
    private int iid;
    private String lastEditedAt;
    private Integer lastEditedById;
    private String mergeCommitSha;
    private String mergeError;
    private MergeParams mergeParams;
    private String mergeStatus;
    private Integer mergeUserId;
    private boolean mergeWhenPipelineSucceeds;
    private Integer milestoneId;
    private String sourceBranch;
    private int sourceProjectId;
    private int stateId;
    private String targetBranch;
    private int targetProjectId;
    private int timeEstimate;
    private String title;
    private String updatedAt;
    private Integer updatedById;
    private String preparedAt;
    private List<Integer> assigneeIds;
    private boolean blockingDiscussionsResolved;
    private String detailedMergeStatus;
    private boolean firstContribution;
    private Object humanTimeChange;
    private Object humanTimeEstimate;
    private Object humanTotalTimeSpent;
    private List<Object> labels;
    private LastCommit lastCommit;
    private List<Integer> reviewerIds;
    private Project source;
    private String state;
    private Project target;
    private int timeChange;
    private int totalTimeSpent;
    private String url;
    private boolean workInProgress;
}

