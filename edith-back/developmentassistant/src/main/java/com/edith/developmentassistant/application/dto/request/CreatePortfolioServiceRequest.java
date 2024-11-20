package com.edith.developmentassistant.application.dto.request;

import com.edith.developmentassistant.application.dto.MergeRequest;
import com.edith.developmentassistant.application.dto.Summary;

import java.util.List;

public record CreatePortfolioServiceRequest (
        String userId,
        String description,
        List<Summary> summaries,
        List<MergeRequest> mergeRequests
){}