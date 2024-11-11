package com.edith.developmentassistant.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortfolioDto {
    private String name;
    private Long projectId;
    private String content;
    private String startDate;
    private String endDate;
    private String portfolio;
}
