package com.edith.developmentassistant.domain.factory;

import com.edith.developmentassistant.domain.model.Branch;
import com.edith.developmentassistant.domain.model.Project;
import com.edith.developmentassistant.domain.model.UserProject;
import com.edith.developmentassistant.application.dto.request.RegisterProjectServiceRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectFactory {

    public static Project createProject(RegisterProjectServiceRequest request , String personalAccessToken) {
        Project project = Project.builder()
                .projectId(request.id())
                .name(request.name())
                .token(personalAccessToken)
                .build();

        List<Branch> branches = request.branches().stream()
                .map(branchName -> createBranch(branchName, project))
                .toList();

        project.updateBranches(branches);

        return project;
    }

    public static Branch createBranch(String branchName, Project project) {
        return Branch.builder()
                .name(branchName)
                .project(project)
                .build();
    }

    public static UserProject createUserProject(RegisterProjectServiceRequest request, Project project) {
        return UserProject.builder()
                .description(request.contents())
                .title(request.name())
                .project(project)
                .build();
    }
}
