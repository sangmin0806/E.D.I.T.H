package com.edith.developmentassistant.controller.dto.response.project;

public record UsersProjectsStats(
        Integer totalProjectsCount,
        Integer todayCommitsCount,
        Integer todayMergeRequestsCount) {
}
