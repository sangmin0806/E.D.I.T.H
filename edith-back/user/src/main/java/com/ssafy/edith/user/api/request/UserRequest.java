package com.ssafy.edith.user.api.request;

public record UserRequest(
        String email,
        String password,
        String gitlabBaseUrl,
        String gitlabPersonalAccessToken) {
}
