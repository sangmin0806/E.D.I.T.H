package com.edith.developmentassistant.controller.dto.response.project;

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

}
