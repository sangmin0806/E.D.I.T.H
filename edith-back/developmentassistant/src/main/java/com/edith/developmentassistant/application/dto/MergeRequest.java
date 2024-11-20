package com.edith.developmentassistant.application.dto;

public record MergeRequest(
        String mrId,
        String userId,
        String filePath,
        String diff
) {
}
