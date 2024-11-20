package com.edith.developmentassistant.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String email;
    private String password;
    private String vcsBaseUrl;
    private boolean vcs;
    private String vcsAccessToken;
}
