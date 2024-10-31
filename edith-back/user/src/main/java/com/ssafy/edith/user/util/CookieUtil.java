package com.ssafy.edith.user.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    private static final int COOKIE_EXPIRATION_TIME = 3600;

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
    public static void addTokenToCookie(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie("accessToken", value);
        cookie.setMaxAge(COOKIE_EXPIRATION_TIME);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}