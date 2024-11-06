package com.edith.developmentassistant.factory;

import com.edith.developmentassistant.domain.Branch;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.domain.UserProject;
import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectFactory {

    public static Project createProject(RegisterProjectServiceRequest request) {
        Project project = Project.builder()
                .projectId(request.projectId())
                .name(request.title())
                .build();

        List<Branch> branches = request.branchesName().stream()
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
                .description(request.description())
                .title(request.title())
                .project(project)
                .build();
    }
}
