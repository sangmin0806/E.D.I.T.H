package com.edith.developmentassistant.service.dto;

import com.edith.developmentassistant.domain.Portfolio;
import com.edith.developmentassistant.domain.UserProject;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class PortfolioDto {
    private String name;
    private Long projectId;
    private String content;
    private String startDate;
    private String endDate;
    private String portfolio;

    public PortfolioDto(UserProject userProject, Portfolio portfolio) {
        this.name = userProject.getTitle();
        this.projectId = userProject.getId();
        this.content = userProject.getDescription();
        this.startDate = portfolio.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
        this.endDate = portfolio.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
        this.portfolio = portfolio.getContent();
    }
}
