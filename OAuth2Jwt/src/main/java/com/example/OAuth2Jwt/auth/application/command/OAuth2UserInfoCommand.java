package com.example.OAuth2Jwt.auth.application.command;

import com.example.OAuth2Jwt.auth.AuthProvider;

public interface OAuth2UserInfoCommand {

    AuthProvider getProvider();

    String getProviderId();

    String getName();

    String getEmail();

    String getNickname();

}
