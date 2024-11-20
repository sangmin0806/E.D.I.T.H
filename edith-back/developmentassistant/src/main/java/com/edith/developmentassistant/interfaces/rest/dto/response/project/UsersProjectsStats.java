package com.edith.developmentassistant.interfaces.rest.dto.response.project;

public record UsersProjectsStats(
        Integer totalProjectsCount,
        Integer todayCommitsCount,
        Integer todayMergeRequestsCount) {
}
