package com.edith.developmentassistant.controller.dto.response.project;

public record ProjectStats(
        Integer totalCommitsCount,
        Integer todayTotalCommitsCount,
        Integer totalCodeReviewCount
) {
}
