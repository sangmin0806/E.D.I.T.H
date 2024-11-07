package com.edith.developmentassistant.service;

import com.edith.developmentassistant.client.dto.UserDto;
import com.edith.developmentassistant.client.user.UserServiceClient;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.factory.ProjectFactory;
import com.edith.developmentassistant.repository.ProjectRepository;
import com.edith.developmentassistant.repository.UserProjectRepository;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final WebhookService webhookService;
    private final UserServiceClient userServiceClient;

    public void registerProject(RegisterProjectServiceRequest request, String token) {
        // TODO : 실제 배포 환경에서는 UserApiClient를 통해 유저 정보를 가져와야 함
//        UserDto user = userApiClient.getUserByToken(token);
        UserDto user = createUserDto();

        String personalAccessToken = user.getVcsAccessToken();
        Long userId = user.getId();

        Project project = projectRepository.findById(request.projectId())
                .orElseGet(() -> createNewProject(request, personalAccessToken));

        updateBranchesIfNeeded(project, request);

        // TODO : 이미 유저가 등록했던 프로젝트인지 확인하는 로직이 필요함
        userProjectRepository.save(createUserProject(request, project, userId));
    }

    private Project createNewProject(RegisterProjectServiceRequest request, String personalAccessToken) {
        webhookService.registerWebhook(request, personalAccessToken);
        return projectRepository.save(ProjectFactory.createProject(request , personalAccessToken));
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
