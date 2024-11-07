package com.edith.developmentassistant.client.dto.mergerequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class References {
    private String shortReference;
    private String relative;
    private String full;
}
