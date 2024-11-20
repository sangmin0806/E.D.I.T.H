package com.edith.developmentassistant.application.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class FindAllPortfolioResponse {

    private String name;
    private String content;
    private String lastModified;
    private String projectId;

    public FindAllPortfolioResponse(String title, String content, LocalDateTime lastModifiedDate, Long projectId) {
        this.name = title;
        this.content = content;
        this.lastModified = lastModifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.projectId = projectId.toString();
    }
}
