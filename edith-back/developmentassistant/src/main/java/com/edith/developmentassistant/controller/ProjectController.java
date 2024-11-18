package com.edith.developmentassistant.controller;

import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.client.dto.gitlab.GitGraph;
import com.edith.developmentassistant.client.rag.RagServiceClient;
import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import com.edith.developmentassistant.controller.dto.request.RegisterProjectRequest;
import com.edith.developmentassistant.controller.dto.response.project.ProjectDashboardDto;
import com.edith.developmentassistant.controller.dto.response.project.ProjectDto;
import com.edith.developmentassistant.controller.dto.response.project.ProjectResponse;
import com.edith.developmentassistant.controller.dto.response.project.ProjectStats;
import com.edith.developmentassistant.controller.dto.response.project.RegisterProjectResponse;
import com.edith.developmentassistant.controller.dto.response.project.UsersProjectsStats;
import com.edith.developmentassistant.service.ProjectService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final RagServiceClient ragServiceClient;


    @PostMapping
    public ApiResult<RegisterProjectResponse> registerProjects(
            @CookieValue(value = "accessToken") String token,
            @RequestBody RegisterProjectRequest registerProjectRequest) {
        projectService.registerProject(registerProjectRequest.toServiceRequest(), token);
        return success(null);
    }

    @GetMapping
    public ApiResult<List<ProjectResponse>> getProjects(
            @CookieValue(value = "accessToken") String token) {
        log.info("ProjectController getProjects token: {}", token);
        return success(projectService.getProjects(token));
    }

    @GetMapping("{projectId}")
    public ApiResult<ProjectResponse> getProject(
            @CookieValue(value = "accessToken") String token,
            @PathVariable Long projectId
    ) {
        return success(projectService.getProjectByTokenAndProjectId(token, projectId));
    }

    @PutMapping
    public ApiResult<ProjectResponse> updateProject(
            @CookieValue(value = "accessToken") String token,
            @RequestBody ProjectDto projectDto) {
        return success(projectService.updateProject(projectDto, token));
    }


    @GetMapping("/gitgraph/{projectId}")
    public ApiResult<List<GitGraph>> getGitGraphData(
            @PathVariable Long projectId,
            @CookieValue(value = "accessToken") String accessToken
    ) {
        List<GitGraph> gitGraphDatas = projectService.getGitGraphData(projectId, accessToken);
        return success(gitGraphDatas);
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "health check";
    }

    @GetMapping("/embedded")
    public String embedded() {
        return ragServiceClient.getHealthCheck();
    }


    @GetMapping("/users/stats")
    public ApiResult<UsersProjectsStats> getUsersProjectsStats(
            @CookieValue(value = "accessToken") String token
    ) {
        return success(projectService.getUsersProjectsStats(token));
    }

    @GetMapping("/stats")
    public ApiResult<ProjectStats> getProjectStats(
            @CookieValue(value = "accessToken") String token,
            @RequestParam @NotNull Long id
    ) {
        return success(projectService.getProjectStats(token, id));
    }

    @GetMapping("/dashboard")
    public ApiResult<ProjectDashboardDto> getProjectDashboard(
            @CookieValue(value = "accessToken") String token,
            @RequestParam @NotNull Long id) {
        return success(projectService.getProjectDashboard(id));
    }
}
