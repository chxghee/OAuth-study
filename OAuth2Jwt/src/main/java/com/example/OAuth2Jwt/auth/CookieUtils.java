package com.example.OAuth2Jwt.auth;

import com.example.OAuth2Jwt.common.exception.ApplicationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

public class CookieUtils {


    // 쿠키에서 refresh token 추출 -> Null 검사
    public static String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new ApplicationException(AuthException.REFRESH_TOKEN_COOKIE_NOT_FOUND);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(AuthException.REFRESH_TOKEN_COOKIE_NOT_FOUND))
                .getValue();
    }

    // 리프레시 토큰 담은 쿠키 생성
    public static Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    // 사용자 쿠키에 저장된 refresh token 만료 시키기
    public static ResponseCookie expireCookie() {
        return ResponseCookie.from("refresh", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();
    }
}
