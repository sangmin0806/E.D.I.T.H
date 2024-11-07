package com.edith.developmentassistant.client.dto.rag;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CodeReviewRequest {
    String url;
    String token;
    String projectId;
    String branch;
    List<CodeReviewChanges> changes;
}
