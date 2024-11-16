package com.edith.developmentassistant.controller.dto.response.project;

import com.edith.developmentassistant.service.dto.DashboardDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectDashboardDto(
        String recentCommitMessage,
        String recentCodeReview,
        String advice,
        List<String> fixLogs,
        List<String> techStack
) {

    public static ProjectDashboardDto from(DashboardDto dashboardDto) {
        return ProjectDashboardDto.builder()
                .recentCommitMessage(dashboardDto.recentCommitMessage())
                .recentCodeReview(dashboardDto.recentCodeReview())
                .advice(dashboardDto.advice())
                .fixLogs(dashboardDto.fixLogs())
                .techStack(dashboardDto.techStack())
                .build();
    }
}
