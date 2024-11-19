package com.edith.developmentassistant.infrastructure.external.gitlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    private String body;
}
