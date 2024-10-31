package com.edith.developmentassistant.controller.dto.request;

import com.edith.developmentassistant.service.dto.request.CreateWebhookServiceRequest;

public record CreateWebhookRequest(
        Integer projectId,
        String pushEventsBranchFilter,
        String personalAccessToken
) {

    public CreateWebhookServiceRequest toCreateWebhookServiceRequest() {
        return new CreateWebhookServiceRequest(projectId, pushEventsBranchFilter, personalAccessToken);
    }
}
