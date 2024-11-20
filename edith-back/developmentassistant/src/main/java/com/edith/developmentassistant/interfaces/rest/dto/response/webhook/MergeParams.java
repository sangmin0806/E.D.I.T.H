package com.edith.developmentassistant.interfaces.rest.dto.response.webhook;

import lombok.Getter;

@Getter
public class MergeParams extends BaseWebhook {

    private String forceRemoveSourceBranch;
    private boolean shouldRemoveSourceBranch;
}
