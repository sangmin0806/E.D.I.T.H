package com.edith.developmentassistant.client.dto.mergerequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TimeStats {
    private int timeEstimate;
    private int totalTimeSpent;
    private String humanTimeEstimate;
    private String humanTotalTimeSpent;
}
