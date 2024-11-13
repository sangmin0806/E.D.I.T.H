package com.edith.developmentassistant.controller.dto.request;

import com.edith.developmentassistant.service.dto.request.RegisterProjectServiceRequest;
import java.util.List;

public record RegisterProjectRequest(
        Long id,
        String title,
        String contents,
        Long userId,
        List<String> branches
) {

    public RegisterProjectServiceRequest toServiceRequest() {
        return new RegisterProjectServiceRequest(
                this.id,
                this.title,
                this.contents,
                this.branches
        );
    }
}

