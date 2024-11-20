package com.edith.developmentassistant.interfaces.rest.dto.request;

import com.edith.developmentassistant.application.dto.request.RegisterProjectServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RegisterProjectRequest(
        @NotNull Long id,
        @NotEmpty String name,
        String contents,
        List<String> branches
) {

    public RegisterProjectServiceRequest toServiceRequest() {
        return new RegisterProjectServiceRequest(
                this.id,
                this.name,
                this.contents,
                this.branches
        );
    }
}

