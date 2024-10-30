package com.ssafy.edith.user.api.request;

public record UserRequest(
        String email,
        String password,
        String vcsBaseUrl,
        String vcsAccessToken) {
}
