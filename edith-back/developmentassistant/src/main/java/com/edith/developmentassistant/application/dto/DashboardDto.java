package com.edith.developmentassistant.application.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardDto {
    Integer projectId;
    String recentCodeReview;
    String recentCommitMessage;
    String advice;
    List<String> fixLogs;
    List<String> techStack;

    public static DashboardDto createInitDashboardDto(Integer projectId) {
        return new DashboardDto(projectId,
                "",
                "",
                "",
                List.of(),
                List.of("Java", "Spring Boot", "React", "MySQL", "Docker", "Python", "Flask"));
    }
}
