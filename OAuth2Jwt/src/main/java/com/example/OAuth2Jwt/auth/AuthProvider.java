package com.example.OAuth2Jwt.auth;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AuthProvider {

    GOOGLE("google", "구글"),
    NAVER("naver", "네이버");

    private final String code;
    private final String displayName;

    AuthProvider(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static AuthProvider from(String code) {
        return Arrays.stream(values())
                .filter(provider -> provider.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 AuthProvider: " + code));
    }
}
