package com.edith.developmentassistant.infrastructure.external.gitlab.dto.mergerequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MRUser {
    private int id;
    private String username;
    private String name;
    private String state;
    private boolean locked;
    private String avatarUrl;
    private String webUrl;
}
