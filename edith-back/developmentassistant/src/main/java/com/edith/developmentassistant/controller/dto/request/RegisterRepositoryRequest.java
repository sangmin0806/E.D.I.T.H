package com.edith.developmentassistant.controller.dto.request;

import com.edith.developmentassistant.service.dto.request.RegisterRepositoryServiceRequest;

public record RegisterRepositoryRequest(
        String url,
        String name,
        String personalAccessToken,
        String description,
        Long projectId,
        String pushEventsBranchFilter,
        boolean codeReview
) {

    public RegisterRepositoryServiceRequest toRegisterRepositoryServiceRequest() {
        return new RegisterRepositoryServiceRequest(
                this.url,
                this.name,
                this.personalAccessToken,
                this.description,
                this.projectId,
                this.pushEventsBranchFilter,
                this.codeReview
        );
    }
}
