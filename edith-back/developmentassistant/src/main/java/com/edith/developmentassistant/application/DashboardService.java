package com.edith.developmentassistant.application;

import com.edith.developmentassistant.application.dto.DashboardDto;
import com.edith.developmentassistant.domain.service.ProjectDomainService;
import com.edith.developmentassistant.infrastructure.client.user.UserServiceClient;
import com.edith.developmentassistant.infrastructure.external.gitlab.GitLabApi;
import com.edith.developmentassistant.infrastructure.repository.jpa.ProjectRepository;
import com.edith.developmentassistant.infrastructure.repository.jpa.UserProjectRepository;
import com.edith.developmentassistant.interfaces.rest.dto.response.project.ProjectDashboardDto;
import com.edith.developmentassistant.interfaces.rest.dto.response.project.ProjectStats;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final CodeReviewService codeReviewService;
    private final UserServiceClient userServiceClient;
    private final GitLabApi gitLabApi;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProjectDomainService projectDomainService;


    public ProjectDashboardDto getProjectDashboard(Long projectId) {

        DashboardDto dashboardDto = (DashboardDto) redisTemplate.opsForValue().get("dashboard:" + projectId);
        log.info("dashboardDto advice : {}", dashboardDto.advice());
        log.info("dashboardDto recentCodeReview : {}", dashboardDto.recentCodeReview());
        log.info("dashboardDto recentCommitMessage : {}", dashboardDto.recentCommitMessage());
        log.info("dashboardDto fixLogs : {}", dashboardDto.fixLogs());
        log.info("dashboardDto techStack : {}", dashboardDto.techStack());

        return getProjectDashboardDto(dashboardDto);
    }

    private static ProjectDashboardDto getProjectDashboardDto(DashboardDto dashboardDto) {
        if (dashboardDto == null) {
            return ProjectDashboardDto.builder()
                    .recentCodeReview("No recent code review")
                    .recentCommitMessage("No recent commit message")
                    .advice("No advice")
                    .fixLogs(List.of())
                    .techStack(List.of())
                    .build();
        }

        return ProjectDashboardDto.from(dashboardDto);
    }

    public ProjectStats getProjectStats(String token, Long projectId) {

        String projectAccessToken = projectDomainService.getProjectAccessToken(projectId);

        Integer todayCommitsCount = getTodayCommitsCount(projectId, projectAccessToken);

        Integer totalMergedRequestsCount = getTotalMergedRequestsCount(projectId, projectAccessToken);

        Integer todayMergeRequestsCount = getTodayMergeRequestsCount(projectId, projectAccessToken);

        log.info("projectService todayCommitsCount: {}", todayCommitsCount);
        log.info("projectService totalMergedRequestsCount: {}", totalMergedRequestsCount);
        log.info("projectService todayMergeRequestsCount: {}", todayMergeRequestsCount);

        return new ProjectStats(todayCommitsCount, totalMergedRequestsCount, todayMergeRequestsCount);
    }

    private Integer getTodayMergeRequestsCount(Long projectId, String projectAccessToken) {
        return gitLabApi.fetchTodayMergeRequestsCount(projectId,
                projectAccessToken);
    }

    private Integer getTotalMergedRequestsCount(Long projectId, String projectAccessToken) {
        return gitLabApi.fetchTotalMergeRequestsCount(projectId,
                projectAccessToken);
    }

    private Integer getTodayCommitsCount(Long projectId, String projectAccessToken) {
        return gitLabApi.fetchTodayCommitsCount(projectId, projectAccessToken);
    }


}
