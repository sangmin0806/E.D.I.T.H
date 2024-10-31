package com.ssafy.edith.user.jwt.valueobject;

public record JwtPayload(Long userId, String email) {
    public static JwtPayload of(Long userId, String email) {
        return new JwtPayload(userId, email);
    }
}
