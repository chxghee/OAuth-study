package com.example.OAuth2Jwt.auth.application.command;

import com.example.OAuth2Jwt.auth.AuthProvider;

import java.util.Map;

public class GoogleUserInfoCommand implements OAuth2UserInfoCommand {

    private final Map<String, Object> attributes;

    public GoogleUserInfoCommand(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
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
        return attributes.get("given_name").toString();
    }
}
