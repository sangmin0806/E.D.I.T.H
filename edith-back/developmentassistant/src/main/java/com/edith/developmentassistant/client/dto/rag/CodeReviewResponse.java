package com.edith.developmentassistant.client.dto.rag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CodeReviewResponse {

    private String review;
    private String status;
    private String summary;
}

