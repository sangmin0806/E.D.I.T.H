package com.edith.developmentassistant.service.dto.request;

import com.edith.developmentassistant.domain.Repository;

public record RegisterRepositoryServiceRequest(
        String url,
        String name,
        String personalAccessToken,
        String description,
        Long projectId,
        String pushEventsBranchFilter,
        boolean codeReview
) {
    public Repository toRepository() {
        return Repository.builder()
                .url(this.url)
                .name(this.name)
                .personalAccessToken(this.personalAccessToken)
                .description(this.description)
                .projectId(this.projectId)
                .pushEventsBranchFilter(this.pushEventsBranchFilter)
                .codeReview(this.codeReview)
                .build();
    }
}