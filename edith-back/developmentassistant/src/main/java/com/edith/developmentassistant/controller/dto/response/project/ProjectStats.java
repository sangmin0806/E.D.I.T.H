package com.edith.developmentassistant.controller.dto.response.project;

public record ProjectStats(
        Integer totalCommitsCount,
        Integer todayCommitsCount,
        Integer todayMergeRequestsCount
) {
}
