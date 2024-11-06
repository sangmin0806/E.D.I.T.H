package com.edith.developmentassistant.controller.dto.response.webhook;

import lombok.Getter;

@Getter
public class MergeParams extends BaseWebhook {

    private String forceRemoveSourceBranch;
    private boolean shouldRemoveSourceBranch;
}
