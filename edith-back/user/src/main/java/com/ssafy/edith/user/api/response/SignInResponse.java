package com.ssafy.edith.user.api.response;

public record SignInResponse(
        Long userId,
        String email,
        String accessToken
) {}