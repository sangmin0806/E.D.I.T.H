package com.edith.developmentassistant.infrastructure.client.rag.rag;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeReviewChanges {
    private String path;
    private String diff;
}
