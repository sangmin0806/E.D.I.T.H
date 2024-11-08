package com.edith.developmentassistant.controller.dto.response.project;

import com.edith.developmentassistant.client.dto.gitlab.ContributorDto;
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
                              List<BranchDto> branches,
                              @JsonFormat(pattern = "yyyy-MM-dd")
                              LocalDateTime updatedAt,
                              List<ContributorSimpleDto> contributors,
                              String content
) {
    public static ProjectResponse from(Project project, List<ContributorDto> contributors, String content) {

        List<ContributorSimpleDto> simpleContributors = contributors.stream()
                .map(ContributorSimpleDto::from)
                .toList();

        return new ProjectResponse(
                project.getId(),
                project.getUrl(),
                project.getName(),
                project.getToken(),
                project.getBranches().stream().map(BranchDto::from).toList(),
                project.getLastModifiedDate(),
                simpleContributors,
                content
        );
    }
}
