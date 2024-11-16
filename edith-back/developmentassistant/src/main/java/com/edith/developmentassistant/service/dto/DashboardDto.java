package com.edith.developmentassistant.service.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record DashboardDto(
        Integer projectId,
        String recentCodeReview,
        String recentCommitMessage,
        String advice,
        List<String> fixLogs,
        List<String> techStack
) {
}
