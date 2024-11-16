package com.edith.developmentassistant.client.dto.rag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CodeReviewResponse {

    private String review;
    private String status;
    private String summary;
    private List<String> techStack;
}
