package com.example.OAuth2Jwt.auth;

import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;


    // OAuth 성공시 실행되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.createAccessToken(customOAuth2User);

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect("http://localhost:3000/");        // 프론트 측의 로그인 성공시 특정 url 주소로 지정해야 함
    }

    // refresh token 저장 용
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24);
        cookie.setSecure(true);         // https 설정
        cookie.setPath("/");            // 쿠키 적용 범위
        cookie.setHttpOnly(true);       // XSS 공격으로 인한 쿠키 탈취 위험 제거

        return cookie;
    }
}
