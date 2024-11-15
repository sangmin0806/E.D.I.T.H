package com.edith.developmentassistant.controller.dto.response.project;

import java.util.List;
import lombok.Builder;

@Builder
public class ProjectDashboardDto {
    private String recentCommitMessage;
    private String recentCodeReview;
    private String advice;
    private List<String> fixLogs;
    private List<String> techStack;
}
