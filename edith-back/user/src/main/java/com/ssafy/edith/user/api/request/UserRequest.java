package com.ssafy.edith.user.api.request;

import com.ssafy.edith.user.entity.User;

public record UserRequest(
        String email,
        String password,
        String vcsBaseUrl,
        String vcsAccessToken) {
    public User toEntity(String encryptedPassword, String encryptedAccessToken) {
        return User.builder()
                .email(this.email)
                .password(encryptedPassword)
                .vcsBaseUrl(this.vcsBaseUrl)
                .vcsAccessToken(encryptedAccessToken)
                .build();
    }
}
