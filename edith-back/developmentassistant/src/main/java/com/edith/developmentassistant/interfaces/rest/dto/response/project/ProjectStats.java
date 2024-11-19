package com.edith.developmentassistant.interfaces.rest.dto.response.project;

public record ProjectStats(
        Integer todayCommitsCount,
        Integer totalMergeRequestsCount,
        Integer todayMergeRequestsCount
) {
}
