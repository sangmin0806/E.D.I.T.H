package com.edith.developmentassistant.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
