package com.edith.developmentassistant.application.dto;

import com.edith.developmentassistant.infrastructure.external.gitlab.dto.ContributorDto;

public record ContributorSimpleDto(String name, String avatarUrl) {
    public static ContributorSimpleDto from(ContributorDto contributor) {
        return new ContributorSimpleDto(contributor.getName(), contributor.getAvatarUrl());
    }
}
