package com.ssafy.edith.user.api.response;

public record SignInResponse(
        Long userId,
        String email,
        String accessToken,
        String username,
        String name,
        String profileImageUrl
) {
    public static SignInResponse of(Long userId, String email, String accessToken,String username, String name, String profileImageUrl) {
        return new SignInResponse(userId, email, accessToken,username, name, profileImageUrl);
    }
}