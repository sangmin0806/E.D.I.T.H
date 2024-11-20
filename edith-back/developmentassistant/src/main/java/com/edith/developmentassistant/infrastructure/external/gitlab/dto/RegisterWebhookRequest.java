package com.edith.developmentassistant.infrastructure.external.gitlab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterWebhookRequest {
    @JsonProperty("url")
    private String url;

    @JsonProperty("description")
    private String description;

    @JsonProperty("push_events")
    private boolean pushEvents;

    @JsonProperty("tag_push_events")
    private boolean tagPushEvents;

    @JsonProperty("merge_requests_events")
    private boolean mergeRequestsEvents;

    @JsonProperty("enable_ssl_verification")
    private boolean enableSslVerification;

    @JsonProperty("push_events_branch_filter")
    private String pushEventsBranchFilter;
}
