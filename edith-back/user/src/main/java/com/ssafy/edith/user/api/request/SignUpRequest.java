package com.ssafy.edith.user.api.request;

import com.ssafy.edith.user.entity.User;

public record SignUpRequest(
        String email,
        String password,
        boolean vcs,
        String vcsBaseUrl,
        String vcsAccessToken) {
    public User toEntity(String encryptedPassword,boolean vcs, String encryptedAccessToken,String vcsBaseUrl) {
        return User.builder()
                .email(this.email)
                .password(encryptedPassword)
                .vcs(vcs)
                .vcsBaseUrl(vcsBaseUrl)
                .vcsAccessToken(encryptedAccessToken)
                .build();
    }
}
