package com.edith.developmentassistant.controller.dto.response.project;

import java.util.List;

public record ProjectDto(
        Long id,
        String url,
        String name,
        String token,
        List<String> branchesName
) {

}
