package com.edith.developmentassistant.domain;

import com.edith.developmentassistant.controller.dto.response.project.ProjectDto;
import com.edith.developmentassistant.factory.ProjectFactory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    private Long id;

    private String url;
    private String name;
    private String token;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MRSummary> mrSummaries;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects;

    @Builder
    private Project(Long projectId, String url, String name, String token) {
        this.id = projectId;
        this.url = url;
        this.name = name;
        this.branches = new ArrayList<>();
        this.token = token;
    }

    public void updateBranches(List<Branch> newBranches) {
        Set<String> existingBranchNames = this.branches.stream()
                .map(Branch::getName)
                .collect(Collectors.toSet());

        List<Branch> branchesToAdd = newBranches.stream()
                .filter(branch -> !existingBranchNames.contains(branch.getName()))
                .toList();

        this.branches.addAll(branchesToAdd);

        this.branches.forEach(branch -> branch.updateProject(this));
    }

    public void updateProject(ProjectDto projectDto) {
        this.url = projectDto.url();
        this.name = projectDto.name();
        this.token = projectDto.token();

        // 현재 브랜치 이름 집합
        Set<String> existingBranchNames = this.branches.stream()
                .map(Branch::getName)
                .collect(Collectors.toSet());

        // 업데이트할 브랜치 이름 집합
        Set<String> newBranchNames = new HashSet<>(projectDto.branchesName());

        // 추가할 브랜치 이름
        Set<String> branchesToAdd = new HashSet<>(newBranchNames);
        branchesToAdd.removeAll(existingBranchNames);

        // 제거할 브랜치 이름
        Set<String> branchesToRemove = new HashSet<>(existingBranchNames);
        branchesToRemove.removeAll(newBranchNames);

        // 브랜치 추가
        branchesToAdd.forEach(branchName -> {
            Branch newBranch = ProjectFactory.createBranch(branchName, this);
            this.branches.add(newBranch);
        });

        // 브랜치 제거
        this.branches.removeIf(branch -> branchesToRemove.contains(branch.getName()));
    }
}
