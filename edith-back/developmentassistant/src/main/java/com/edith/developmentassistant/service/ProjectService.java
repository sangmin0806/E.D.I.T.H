package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.gitlab.GitLabServiceClient;
import com.edith.developmentassistant.client.user.UserServiceClient;
import com.edith.developmentassistant.client.dto.gitlab.GitCommit;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.factory.ProjectFactory;
import com.edith.developmentassistant.repository.ProjectRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // TODO : Project Access Token 생성
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

    public List<GitCommit> fetchGitLabCommits(Long projectId, String accessToken){
        UserDto userDto = userServiceClient.getUserByToken(accessToken);
        List<GitCommit> commits = gitLabServiceClient.fetchGitLabCommits(projectId,userDto.getVcsAccessToken());
        return commits;
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
}
