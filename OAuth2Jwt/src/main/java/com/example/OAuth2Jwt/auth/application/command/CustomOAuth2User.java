package com.example.OAuth2Jwt.auth.application.command;

import com.example.OAuth2Jwt.member.application.MemberAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberAuthInfo memberAuthInfo;

    // 사용 x -> 구글 네이버 등의 attributes가 일치하지 않기 때문에 앞서 service 에서 다른 데이터 구조로 유저 정보를 담는다
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    // 유저 Role 값
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberAuthInfo.role().getValue()));
    }

    // 유저 특정 가능한 식별자
    @Override
    public String getName() {
        return String.valueOf(memberAuthInfo.id());
    }
}
