package com.ssafy.edith.user.api.response;

public record SignInResponse(
        Long userId,
        String email,
        String accessToken
) {
    public static SignInResponse of(Long userId, String email, String accessToken) {
        return new SignInResponse(userId, email, accessToken);
    }
}