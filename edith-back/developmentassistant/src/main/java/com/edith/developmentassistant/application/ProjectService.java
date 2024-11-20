package com.edith.developmentassistant.application;

import com.edith.developmentassistant.application.dto.request.RegisterProjectServiceRequest;
import com.edith.developmentassistant.domain.factory.ProjectFactory;
import com.edith.developmentassistant.domain.model.Project;
import com.edith.developmentassistant.domain.model.UserProject;
import com.edith.developmentassistant.domain.service.ProjectDomainService;
import com.edith.developmentassistant.infrastructure.client.user.UserServiceClient;
import com.edith.developmentassistant.infrastructure.dto.UserDto;
import com.edith.developmentassistant.infrastructure.external.gitlab.GitLabApi;
import com.edith.developmentassistant.infrastructure.external.gitlab.dto.ContributorDto;
import com.edith.developmentassistant.infrastructure.external.gitlab.dto.GitCommit;
import com.edith.developmentassistant.infrastructure.external.gitlab.dto.GitGraph;
import com.edith.developmentassistant.infrastructure.external.gitlab.dto.GitMerge;
import com.edith.developmentassistant.infrastructure.repository.jpa.ProjectRepository;
import com.edith.developmentassistant.infrastructure.repository.jpa.UserProjectRepository;
import com.edith.developmentassistant.interfaces.rest.dto.response.project.ProjectDto;
import com.edith.developmentassistant.interfaces.rest.dto.response.project.ProjectResponse;
import com.edith.developmentassistant.interfaces.rest.dto.response.project.UsersProjectsStats;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final CodeReviewService codeReviewService;
    private final UserServiceClient userServiceClient;
    private final GitLabApi gitLabApi;
    private final ProjectDomainService projectDomainService;

    public void registerProject(RegisterProjectServiceRequest request, String token) {

        UserDto user = userServiceClient.getUserByToken(token);
        String personalAccessToken = user.getVcsAccessToken();
        Long userId = user.getUserId();

        String projectAccessToken = gitLabApi.generateProjectAccessToken(request.id(), personalAccessToken);

        Project project = getProjectByTokenAndProjectId(request, projectAccessToken);

        updateBranchesIfNeeded(project, request);

        log.info("request.name: {}", request.name());

        userProjectRepository.save(createUserProject(request, project, userId));
    }


    public List<ProjectResponse> getProjects(String token) {
        return getProjectsResponses(token, getProjectIds(getUserProjectsBy(getUserIdByToken(token))));
    }

    private static List<Long> getProjectIds(List<UserProject> userProjects) {
        return userProjects.stream()
                .map(userProject -> userProject.getProject().getId())
                .toList();
    }

    public List<GitGraph> getGitGraphData(Long projectId, String accessToken) {

        String projectAccessToken = projectDomainService.getProjectAccessToken(projectId);

        List<GitMerge> merges = gitLabApi.fetchGitLabMergeRequests(projectId, projectAccessToken);

        return getGitGraphList(projectId, merges, projectAccessToken);
    }

    private List<GitGraph> getGitGraphList(Long projectId, List<GitMerge> merges, String projectAccessToken) {
        return merges.stream()
                .map(mergeRequest -> {
                    // MergeRequest 커밋 가져오기
                    GitCommit mergeCommit = gitLabApi.fetchCommitDetails(projectId,
                            mergeRequest.getMerge_commit_sha(), projectAccessToken);

                    // 해당 MergeRequest내 전체 커밋을 가져오기
                    List<GitCommit> sourceBranchCommits = gitLabApi.fetchCommitsInMergeRequest(
                            projectId,
                            mergeRequest.getIid(),
                            projectAccessToken
                    );

                    return GitGraph.of(
                            mergeRequest.getSource_branch(),
                            mergeRequest.getTarget_branch(),
                            mergeCommit,
                            sourceBranchCommits
                    );
                })
                .toList();
    }


    public UserProject findUserProjectByUserIdAndProjectId(Long userId, Long projectId) {
        return userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElse(null);
    }

    private Project createNewProject(RegisterProjectServiceRequest request, String personalAccessToken) {
        codeReviewService.registerWebhook(request, personalAccessToken);
        return projectRepository.save(ProjectFactory.createProject(request, personalAccessToken));
    }

    private void updateBranchesIfNeeded(Project project, RegisterProjectServiceRequest request) {
        project.updateBranches(request.branches().stream()
                .map(branchName -> ProjectFactory.createBranch(branchName, project))
                .toList());
    }


    public ProjectResponse getProjectByTokenAndProjectId(String token, Long projectId) {

        UserProject userProject = getUserProjectByProjectIdAndUserId(projectId, getUserIdByToken(token));

        Project project = userProject.getProject();

        List<ContributorDto> contributors = gitLabApi.fetchContributors(project.getId(), project.getToken());

        log.info("contributors: {}", contributors);
        log.info("project: {}", project);
        log.info("userProject: {}", userProject);

        return ProjectResponse.from(project, userProject.getTitle(), contributors, userProject.getDescription());
    }

    public ProjectResponse updateProject(ProjectDto projectDto, String token) {

        List<UserProject> userProjects = getUserProjectsBy(getUserIdByToken(token));

        String content = "";
        String projectName = "";
        Project projectToUpdate = null;

        UserProject userProject1 = userProjects.stream()
                .filter(userProject -> userProject.getProject().getId().equals(projectDto.id()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Project not found for the user"));

        projectToUpdate = userProject1.getProject();
        projectName = userProject1.getTitle();
        content = userProject1.getDescription();

        ifProjectNullReturnRunTimeException(projectToUpdate);

        projectToUpdate.updateProject(projectDto);

        projectRepository.save(projectToUpdate);

        return ProjectResponse.from(projectToUpdate, projectName, null, content);
    }

    private static void ifProjectNullReturnRunTimeException(Project projectToUpdate) {
        if (projectToUpdate == null) {
            throw new IllegalArgumentException("Project not found for the user");
        }
    }

    private List<UserProject> getUserProjectsBy(Long token) {
        return userProjectRepository.findByUserId(token).stream().toList();
    }


    private UserProject getUserProjectByProjectIdAndUserId(Long projectId, Long userId) {
        return userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found for the user"));
    }

    private Long getUserIdByToken(String token) {
        UserDto userByToken = userServiceClient.getUserByToken(token);
        return userByToken.getUserId();
    }

    private String getUserEmailByToken(String token) {
        UserDto userByToken = userServiceClient.getUserByToken(token);
        return userByToken.getEmail();
    }

    private List<ProjectResponse> getProjectsResponses(String token, List<Long> projectIds) {
        return projectIds.stream()
                .map(projectId -> getProjectByTokenAndProjectId(token, projectId))
                .toList();
    }

    private Project getProjectByTokenAndProjectId(RegisterProjectServiceRequest request, String projectAccessToken) {
        return projectRepository.findById(request.id())
                .orElseGet(() -> createNewProject(request, projectAccessToken));
    }

    private UserProject createUserProject(RegisterProjectServiceRequest request, Project project, Long userId) {
        log.info("Creating UserProject for userId: {}, id: {}, project-name: {}", userId, project.getId(),
                request.name());
        return getUserProject(request, project, userId);
    }

    private static UserProject getUserProject(RegisterProjectServiceRequest request, Project project, Long userId) {
        return UserProject.builder()
                .userId(userId)
                .description(request.contents())
                .title(request.name())
                .project(project)
                .build();
    }

    public UsersProjectsStats getUsersProjectsStats(String token) {
        log.info("developmentassisstant service에서 보내는 JWT Token: {}", token);
        Long userId = getUserIdByToken(token);
        String userEmail = getUserEmailByToken(token);
        String personalAccessToken = getVcsAccessToken(token);

        List<UserProject> userProjects = getUserProjectsBy(userId);

        Integer totalProjectsCount = userProjects.size();

        Integer todayCommitsCount = getUserTodayCommitsCount(userProjects, userEmail, personalAccessToken);

        Integer todayMergeRequestsCount = getUserTodayMergeRequestsCount(userProjects, userEmail, personalAccessToken);

        return new UsersProjectsStats(totalProjectsCount, todayCommitsCount, todayMergeRequestsCount);
    }

    private String getVcsAccessToken(String token) {
        return userServiceClient.getUserByToken(token).getVcsAccessToken();
    }


    private Integer getUserTodayCommitsCount(List<UserProject> userProjects, String userEmail,
                                             String personalAccessToken) {
        return userProjects.stream()
                .map(userProject -> getUserCommitsCount(userEmail, personalAccessToken, userProject))
                .reduce(0, Integer::sum);
    }

    private Integer getUserCommitsCount(String userEmail, String personalAccessToken, UserProject userProject) {
        return gitLabApi.fetchTodayUserCommitsCount(userProject.getProject().getId(),
                personalAccessToken, userEmail);
    }

    private Integer getUserTodayMergeRequestsCount(List<UserProject> userProjects, String userEmail,
                                                   String personalAccessToken) {
        return userProjects.stream()
                .map(userProject -> getTodayUserMergeRequestsCount(userEmail, personalAccessToken, userProject))
                .reduce(0, Integer::sum);
    }

    private Integer getTodayUserMergeRequestsCount(String userEmail, String personalAccessToken, UserProject userProject) {
        return gitLabApi.fetchTodayUserMergeRequestsCount(
                userProject.getProject().getId(),
                personalAccessToken, userEmail);
    }
}
