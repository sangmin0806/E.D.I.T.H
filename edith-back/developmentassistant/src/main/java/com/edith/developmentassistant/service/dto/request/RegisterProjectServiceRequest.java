package com.edith.developmentassistant.service.dto.request;

import java.util.List;

public record RegisterProjectServiceRequest(
        Long projectId,
        String title,
        String description,
        List<String> branchesName
){}

