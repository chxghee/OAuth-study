package com.example.OAuth2Jwt.member.application;

import com.example.OAuth2Jwt.member.domain.Member;
import com.example.OAuth2Jwt.member.domain.Role;
import io.jsonwebtoken.Claims;

public record MemberAuthInfo(
        Long id,
        String email,
        String nickname,
        Role role
) {
    public static MemberAuthInfo fromOAuth2UserInfo(Member member) {
        return new MemberAuthInfo(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole()
        );
    }

    public static MemberAuthInfo fromClaims(Claims claims) {
        return new MemberAuthInfo(
                Long.valueOf(claims.get("id", String.class)),
                claims.get("email", String.class),
                claims.get("nickname", String.class),
                Role.from(claims.get("role", String.class))
        );
    }
}
