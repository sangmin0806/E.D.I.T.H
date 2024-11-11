package com.edith.developmentassistant.service.dto;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MergeRequestDateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<MergeRequest> mergeRequests;

    public MergeRequestDateRange(LocalDateTime startDate, LocalDateTime endDate, List<MergeRequest> mergeRequests) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.mergeRequests = mergeRequests;
    }

    @Override
    public String toString() {
        return startDate.toString() + " - " + endDate.toString();
    }
}