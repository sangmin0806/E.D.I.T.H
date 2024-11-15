package com.edith.developmentassistant.controller.dto.response.project;

public record ProjectStats(
        Integer todayCommitsCount,
        Integer totalMergeRequestsCount,
        Integer todayMergeRequestsCount
) {
}
