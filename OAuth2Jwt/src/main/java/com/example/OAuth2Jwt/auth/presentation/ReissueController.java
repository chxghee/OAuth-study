package com.example.OAuth2Jwt.auth.presentation;

import com.example.OAuth2Jwt.auth.CookieUtils;
import com.example.OAuth2Jwt.auth.application.ReissueService;
import com.example.OAuth2Jwt.auth.application.command.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @GetMapping("/reissue")
    public ResponseEntity<JwtToken> reissue(HttpServletRequest request, HttpServletResponse response) {

        log.info("리다이렉트 후 억세스 토큰 발급");

        // 1. 쿠키에서 refresh token 추출 -> Null 검사
        String refreshToken = CookieUtils.getRefreshToken(request);

        JwtToken jwtToken = reissueService.reissueToken(refreshToken);
        Cookie cookie = CookieUtils.createRefreshTokenCookie(jwtToken.refreshToken());
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken.accessToken())
                .body(jwtToken);
    }
}
