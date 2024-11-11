package com.edith.developmentassistant.service.dto.request;

import java.util.List;

public record RegisterProjectServiceRequest(
        Long id,
        String name,
        String contents,
        List<String> branches
){}

