package com.edith.developmentassistant.controller.dto.response.project;

import com.edith.developmentassistant.domain.Project;
import com.edith.developmentassistant.service.dto.BranchDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(Long id,
                              String url,
                              String name,
                              String token,
                              List<BranchDto> branches,
                              @JsonFormat(pattern = "yyyy-MM-dd")
                              LocalDateTime updatedAt
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(project.getId(), project.getUrl(), project.getName(), project.getToken(),
                project.getBranches().stream().map(BranchDto::from).toList(), project.getLastModifiedDate());
    }
}
