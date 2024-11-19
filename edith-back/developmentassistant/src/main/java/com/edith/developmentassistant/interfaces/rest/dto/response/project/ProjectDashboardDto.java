package com.edith.developmentassistant.interfaces.rest.dto.response.project;

import com.edith.developmentassistant.application.dto.DashboardDto;
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
