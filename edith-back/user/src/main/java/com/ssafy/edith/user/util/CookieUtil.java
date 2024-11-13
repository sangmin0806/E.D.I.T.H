package com.ssafy.edith.user.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private final int cookieExpiration;

    public CookieUtil(@Value("${app.cookie.expiration}") int cookieExpiration) {
        this.cookieExpiration = cookieExpiration;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public void addAccessToken(HttpServletResponse response, String value) {
        ResponseCookie cookie = ResponseCookie.from("accessToken",value)
                        .path("/")
                        .sameSite("None")
                        .httpOnly(true)
                        .secure(true)
                        .maxAge(cookieExpiration)
                        .build();
        response.addHeader("Set-Cookie", cookie.toString());

    }
    public void addRefreshToken(HttpServletResponse response, String value) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken",value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(cookieExpiration)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}