package com.edith.developmentassistant.controller.dto.response;

public record RegisterRepositoryResponse(
        String name,
        String description,
        Long projectId,
        String pushEventsBranchFilter,
        boolean codeReview
) {
}
