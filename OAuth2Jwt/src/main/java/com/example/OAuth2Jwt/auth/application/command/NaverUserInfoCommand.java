package com.example.OAuth2Jwt.auth.application.command;

import com.example.OAuth2Jwt.auth.AuthProvider;

import java.util.Map;

public class NaverUserInfoCommand implements OAuth2UserInfoCommand {

    private final Map<String, Object> attributes;

    // naver 의 경우에는 로그인 서버 응답이 response 에 감싸져서 옴
    public NaverUserInfoCommand(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("nickname").toString();
    }
}
