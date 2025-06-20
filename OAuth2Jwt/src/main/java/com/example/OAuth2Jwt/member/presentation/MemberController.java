package com.example.OAuth2Jwt.member.presentation;

import com.example.OAuth2Jwt.auth.CookieUtils;
import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import com.example.OAuth2Jwt.member.application.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("mypage")
    public ResponseEntity<CustomOAuth2User> mypage(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(customOAuth2User);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        // 1. 쿠키에서 refresh token 추출 -> Null 검사
        String refreshToken = CookieUtils.getRefreshToken(request);
        memberService.logout(refreshToken);

        ResponseCookie expiredCookie = CookieUtils.expireCookie();
        response.setHeader("Set-Cookie", expiredCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
