package com.edith.developmentassistant.domain;

import com.edith.developmentassistant.controller.dto.response.RegisterProjectResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MRSummary> mrSummaries;

    @Builder
    private Project(Long projectId, String url, String name) {
        this.id = projectId;
        this.url = url;
        this.name = name;
        this.branches = new ArrayList<>();
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
}
