package com.edith.developmentassistant.service.dto.request;

public record CreateWebhookServiceRequest(
        Integer projectId,
        String pushEventsBranchFilter,
        String personalAccessToken
) {
}
