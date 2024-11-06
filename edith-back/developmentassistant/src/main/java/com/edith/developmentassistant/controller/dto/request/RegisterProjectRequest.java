package com.edith.developmentassistant.controller.dto.request;

import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import java.util.List;

public record RegisterProjectRequest(
        Long projectId,
        String title,
        String description,
        Long userId,
        List<String> branches
) {

    public RegisterProjectServiceRequest toServiceRequest() {
        return new RegisterProjectServiceRequest(
                this.projectId,
                this.title,
                this.description,
                this.branches
        );
    }
}

