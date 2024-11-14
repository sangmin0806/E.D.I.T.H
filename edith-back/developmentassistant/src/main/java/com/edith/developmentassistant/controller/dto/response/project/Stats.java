package com.edith.developmentassistant.controller.dto.response.project;

public record Stats(
        Integer totalCommitsCount,
        Integer todayTotalCommitsCount,
        Integer totalMergeRequestsCount
) {
}
