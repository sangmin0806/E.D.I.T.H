package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.dto.gitlab.ContributorDto;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.client.user.UserServiceClient;
import com.edith.developmentassistant.controller.dto.response.project.ProjectDto;
import com.edith.developmentassistant.controller.dto.response.project.ProjectResponse;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.factory.ProjectFactory;
import com.edith.developmentassistant.repository.ProjectRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
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
    private final WebhookService webhookService;
    private final UserServiceClient userServiceClient;
    private final GitLabServiceClient gitLabServiceClient;

    public void registerProject(RegisterProjectServiceRequest request, String token) {
        // TODO : 실제 배포 환경에서는 UserApiClient를 통해 유저 정보를 가져와야 함
//        UserDto user = userApiClient.getUserByToken(token);
        UserDto user = createUserDto();

        String personalAccessToken = user.getVcsAccessToken();
        Long userId = user.getId();

        String projectAccessToken = gitLabServiceClient.generateProjectAccessToken(request.projectId(),
                personalAccessToken);

        Project project = projectRepository.findById(request.projectId())
                .orElseGet(() -> createNewProject(request, projectAccessToken));

        updateBranchesIfNeeded(project, request);

        // TODO : 이미 유저가 등록했던 프로젝트인지 확인하는 로직이 필요함
        userProjectRepository.save(createUserProject(request, project, userId));
    }


    public List<ProjectResponse> getProjects(String token) {
        // TODO : 배포 환경에서 주석 해제 후 사용
//        UserDto userByToken = userServiceClient.getUserByToken(token);
//        Long userId = userByToken.getId();
        Long userId = 1L;
        List<UserProject> userProjects = userProjectRepository.findByUserId(userId);
        return userProjects.stream()
                .map(userProject -> {
                    Project project = userProject.getProject();

                    // GitLab에서 프로젝트의 기여자 목록 가져오기
                    List<ContributorDto> contributors = gitLabServiceClient.fetchContributors(project.getId(),
                            project.getToken());

                    // 프로젝트와 기여자 리스트를 사용하여 ProjectResponse 생성
                    return ProjectResponse.from(project, userProject.getTitle(), contributors,
                            userProject.getDescription());
                })
                .toList();
    }

    public List<GitCommit> fetchGitLabCommits(Long projectId, String accessToken) {
        UserDto userDto = userServiceClient.getUserByToken(accessToken);
        String projectAccessToken = gitLabServiceClient.generateProjectAccessToken(projectId,
                userDto.getVcsAccessToken());
        List<GitCommit> commits = gitLabServiceClient.fetchGitLabCommits(projectId, projectAccessToken);
        return commits;
    }

    public UserProject findUserProjectByUserIdAndProjectId(Long userId, Long projectId) {
        return userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElse(null);
    }

    private Project createNewProject(RegisterProjectServiceRequest request, String personalAccessToken) {
        webhookService.registerWebhook(request, personalAccessToken);
        return projectRepository.save(ProjectFactory.createProject(request, personalAccessToken));
    }

    private void updateBranchesIfNeeded(Project project, RegisterProjectServiceRequest request) {
        project.updateBranches(request.branchesName().stream()
                .map(branchName -> ProjectFactory.createBranch(branchName, project))
                .toList());
    }

    private UserProject createUserProject(RegisterProjectServiceRequest request, Project project, Long userId) {
        return UserProject.builder()
                .userId(userId)
                .description(request.description())
                .title(request.title())
                .project(project)
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .id(1L)
                .email("marmong9770@gmail.com")
                .password("1234")
                .vcsBaseUrl("https://lab.ssafy.com/")
                .vcsAccessToken("NHMeAABxUvZVyLq6u5Qx")
                .build();
    }


    public ProjectResponse updateProject(ProjectDto projectDto, String token) {
        // TODO : 배포 환경에서 주석 해제 후 사용
//    UserDto userByToken = userServiceClient.getUserByToken(token);
//    Long userId = userByToken.getId();
        Long userId = 1L;
        List<UserProject> userProjects = userProjectRepository.findByUserId(userId);
        String content = "";
        if (userProjects == null || userProjects.isEmpty()) {
            throw new IllegalArgumentException("Illegal user");
        }
        String projectName = "";
        Project projectToUpdate = null;
        for (UserProject userProject : userProjects) {
            if (userProject.getProject().getId().equals(projectDto.id())) {
                projectToUpdate = userProject.getProject();
                content = userProject.getDescription();
                projectName = userProject.getTitle();
                break;
            }
        }

        if (projectToUpdate == null) {
            throw new IllegalArgumentException("Project not found for the user");
        }

        // 프로젝트 업데이트
        projectToUpdate.updateProject(projectDto);

        // 변경 사항 저장
        projectRepository.save(projectToUpdate);

        return ProjectResponse.from(projectToUpdate, projectName, null, content);
    }

    public ProjectResponse getProject(String token, Long projectId) {
        // TODO : 배포 환경에서 주석 해제 후 사용
//        UserDto userByToken = userServiceClient.getUserByToken(token);
//        Long userId = userByToken.getId();
        Long userId = 1L;

        UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found for the user"));

        Project project = userProject.getProject();

        List<ContributorDto> contributors = gitLabServiceClient.fetchContributors(project.getId(), project.getToken());

        return ProjectResponse.from(project, userProject.getTitle(), contributors, userProject.getDescription());
    }
}
