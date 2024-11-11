package com.edith.developmentassistant.service.dto.response;

import com.edith.developmentassistant.domain.Portfolio;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePortfolioResponse {
    private String name;
    private Long projectId;
    private String content;
    private String startDate;
    private String endDate;
    private String portfolio;
}
