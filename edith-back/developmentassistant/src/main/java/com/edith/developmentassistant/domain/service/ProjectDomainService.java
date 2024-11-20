package com.edith.developmentassistant.domain.service;

import com.edith.developmentassistant.domain.model.Project;
import com.edith.developmentassistant.infrastructure.repository.jpa.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDomainService {

    private final ProjectRepository projectRepository;

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public String getProjectAccessToken(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"))
                .getToken();
    }
}
