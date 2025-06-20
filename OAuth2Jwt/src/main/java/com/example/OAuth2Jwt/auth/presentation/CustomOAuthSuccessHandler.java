package com.example.OAuth2Jwt.auth.presentation;

import com.example.OAuth2Jwt.auth.JwtTokenProvider;
import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import com.example.OAuth2Jwt.auth.domain.RefreshToken;
import com.example.OAuth2Jwt.auth.domain.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    // OAuth 성공시 실행되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customOAuth2User.getMemberAuthInfo().id();

        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
        refreshTokenRepository.save(new RefreshToken(memberId, refreshToken));

        log.info("로그인 성공: {}", customOAuth2User.getMemberAuthInfo().toString());
        log.info("Refresh token created: {}", refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);

        response.addCookie(createCookie("refresh", refreshToken));  // 쿠키 등록
        response.sendRedirect("http://localhost:3000/oauth2/redirect");        // 프론트 측의 로그인 성공시 특정 url 주소로 지정해야 함
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
