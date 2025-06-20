package com.example.OAuth2Jwt.auth;

import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import com.example.OAuth2Jwt.member.application.MemberAuthInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 10;         // 10 분 설정
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24;   //  24 시간 설정

    // HS256(대칭키 기반) 비밀 키를 생성
    public JwtTokenProvider(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // 억세스 토큰 생성
    public String createAccessToken(CustomOAuth2User user) {
        return Jwts.builder()
                .claim("id", String.valueOf(user.getMemberAuthInfo().id()))
                .claim("email", user.getMemberAuthInfo().email())
                .claim("nickname", user.getMemberAuthInfo().nickname())
                .claim("role", user.getMemberAuthInfo().role().name())
                .claim("category", "access")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();
    }

    // 억세스 토큰에서 유저 정보 가져옴
    public Authentication getAuthentication(String bearerToken) {

        String accessToken = getAccessToken(bearerToken);
        Claims claims = getClaims(accessToken);

        String category = claims.get("category", String.class);
        if (!category.equals("access")) {
            throw new IllegalArgumentException("access 토큰이 비어있습니다.");
        }

        MemberAuthInfo memberAuthInfo = MemberAuthInfo.fromClaims(claims);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberAuthInfo);

        return new UsernamePasswordAuthenticationToken(
                customOAuth2User, null, customOAuth2User.getAuthorities()
        );
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static String getAccessToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank() || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("JWT 토큰이 비어있거나 형식이 잘못되었습니다.");
        }
        return bearerToken.substring(7);
    }

}
