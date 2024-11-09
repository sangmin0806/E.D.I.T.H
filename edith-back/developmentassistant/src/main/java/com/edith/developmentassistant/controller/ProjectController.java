package com.edith.developmentassistant.controller;

import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.client.dto.gitlab.GitBranch;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.edith.developmentassistant.client.dto.gitlab.GitGraph;
import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import com.edith.developmentassistant.controller.dto.request.RegisterProjectRequest;
import com.edith.developmentassistant.controller.dto.response.gitlab.GitLabBranchesResponse;
import com.edith.developmentassistant.controller.dto.response.gitlab.GitLabCommitsResponse;
import com.edith.developmentassistant.controller.dto.response.project.ProjectDto;
import com.edith.developmentassistant.controller.dto.response.project.ProjectResponse;
import com.edith.developmentassistant.controller.dto.response.project.RegisterProjectResponse;
import com.edith.developmentassistant.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final GitLabServiceClient gitlabServiceClient;

    @PostMapping
    public ApiResult<RegisterProjectResponse> registerProjects(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody RegisterProjectRequest registerProjectRequest) {
        projectService.registerProject(registerProjectRequest.toServiceRequest(), token);
        return success(null);
    }

    @GetMapping
    public ApiResult<List<ProjectResponse>> getProjects(
            @CookieValue(value = "accessToken", required = false) String token) {
        return success(projectService.getProjects(token));
    }

    @GetMapping("{projectId}")
    public ApiResult<ProjectResponse> getProjects(
            @CookieValue(value = "accessToken", required = false) String token,
            @PathVariable Long projectId
    ) {
        return success(projectService.getProject(token, projectId));
    }

    @PutMapping
    public ApiResult<ProjectResponse> updateProject(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody ProjectDto projectDto) {
        return success(projectService.updateProject(projectDto, token));
    }
    @GetMapping("/gitgraph/{projectId}")
    public ApiResult<List<GitGraph>> getGitGraphData(
            @PathVariable Long projectId,
            @CookieValue(value = "accessToken", required = false)  String accessToken
    ) {
        List<GitGraph> gitGraphDatas = projectService.getGitGraphData(projectId, accessToken);
        return success(gitGraphDatas);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health check";
    }

    @PostMapping("/test")
    public void test() {
        String review = """
                리뷰입니다.
                """;
        gitlabServiceClient.addMergeRequestComment(824085L, 64L, "TWD9FX7P7Qc1bYqyo_cC", review);
    }
}