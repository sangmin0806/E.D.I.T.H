package com.edith.developmentassistant.client.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String vcsBaseUrl;
    private String vcsAccessToken;
}
