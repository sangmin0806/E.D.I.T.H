package com.edith.developmentassistant.client.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDto {

    private Long userId;
    private String email;
    private String password;
    private String vcsBaseUrl;
    private boolean vcs;
    private String vcsAccessToken;
}
