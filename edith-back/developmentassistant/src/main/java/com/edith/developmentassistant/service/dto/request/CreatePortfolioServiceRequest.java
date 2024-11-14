package com.edith.developmentassistant.service.dto.request;

import com.edith.developmentassistant.service.dto.MergeRequest;
import com.edith.developmentassistant.service.dto.Summary;

import java.util.List;

public record CreatePortfolioServiceRequest (
        String userId,
        String description,
        List<Summary> summaries,
        List<MergeRequest> mergeRequests
){}