package com.edith.developmentassistant.service.dto;

public record MergeRequest(
        String mrId,
        String userId,
        String filePath,
        String diff
) {
}
