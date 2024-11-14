package com.edith.developmentassistant.controller.dto.response.project;

import com.edith.developmentassistant.client.dto.gitlab.ContributorDto;
import com.edith.developmentassistant.domain.Branch;
import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.service.dto.BranchDto;
import com.edith.developmentassistant.service.dto.ContributorSimpleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(Long id,
                              String url,
                              String name,
                              String token,
                              List<String> branches,
                              @JsonFormat(pattern = "yyyy-MM-dd")
                              LocalDateTime updatedAt,
                              List<ContributorSimpleDto> contributors,
                              String content
) {
    public static ProjectResponse from(Project project, String projectName, List<ContributorDto> contributors,
                                       String content) {

        List<ContributorSimpleDto> simpleContributors = contributors.stream()
                .map(ContributorSimpleDto::from)
                .toList();

        return new ProjectResponse(
                project.getId(),
                project.getUrl(),
                projectName,
                project.getToken(),
                project.getBranches().stream().map(Branch::getName).toList(),
                project.getLastModifiedDate(),
                simpleContributors,
                content
        );
    }
}
