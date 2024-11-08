package com.edith.developmentassistant.service.dto;

import com.edith.developmentassistant.client.dto.gitlab.ContributorDto;

public record ContributorSimpleDto(String name, String avatarUrl) {
    public static ContributorSimpleDto from(ContributorDto contributor) {
        return new ContributorSimpleDto(contributor.getName(), contributor.getAvatarUrl());
    }
}
