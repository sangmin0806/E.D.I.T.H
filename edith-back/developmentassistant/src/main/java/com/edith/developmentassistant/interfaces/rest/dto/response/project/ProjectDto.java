package com.edith.developmentassistant.interfaces.rest.dto.response.project;

import java.util.List;

public record ProjectDto(
        Long id,
        String name,
        String token,
        List<String> branches
) {
}
