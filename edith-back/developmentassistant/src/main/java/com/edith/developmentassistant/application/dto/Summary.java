package com.edith.developmentassistant.application.dto;

import com.edith.developmentassistant.domain.model.MRSummary;

public record Summary (
        String mrId,
        String content
){
    public static Summary from(MRSummary mrSummary) {
        return new Summary(mrSummary.getMrId(), mrSummary.getContent());
    }
}