package com.edith.developmentassistant.controller;

import static com.edith.developmentassistant.controller.ApiUtils.success;

import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.controller.ApiUtils.ApiResult;
import com.edith.developmentassistant.controller.dto.request.RegisterProjectRequest;
import com.edith.developmentassistant.controller.dto.response.project.ProjectDto;
import com.edith.developmentassistant.controller.dto.response.project.ProjectResponse;
import com.edith.developmentassistant.controller.dto.response.project.RegisterProjectResponse;
import com.edith.developmentassistant.domain.Branch;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.controller.dto.response.RegisterProjectResponse;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.edith.developmentassistant.controller.dto.response.gitlab.GitLabCommitsResponse;
import com.edith.developmentassistant.service.ProjectService;
import com.edith.developmentassistant.service.dto.BranchDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/commits/{projectId}")
    public ApiResult<GitLabCommitsResponse> getGitLabCommits(
            @PathVariable Long projectId,
            @CookieValue(value = "accessToken", required = false) String token) {

        List<GitCommit> commits = projectService.fetchGitLabCommits(projectId, token);

        return success(GitLabCommitsResponse.of(commits));
    }

    @GetMapping
    public ApiResult<List<ProjectResponse>> getProjects(
            @CookieValue(value = "accessToken", required = false) String token) {
        return success(projectService.getProjects(token));
    }

    @PutMapping
    public ApiResult<ProjectResponse> updateProject(
            @CookieValue(value = "accessToken", required = false) String token,
            @RequestBody ProjectDto projectDto) {
        return success(projectService.updateProject(projectDto, token));
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