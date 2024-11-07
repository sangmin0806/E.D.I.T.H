package com.edith.developmentassistant.service.dto;

import com.edith.developmentassistant.domain.MRSummary;

public record Summary (
        String mrId,
        String content
){
    public static Summary from(MRSummary mrSummary) {
        return new Summary(mrSummary.getMrId(), mrSummary.getContent());
    }
}