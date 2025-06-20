package com.example.OAuth2Jwt.auth.application.command;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
    public static JwtToken of(String accessToken, String refreshToken) {
        return new JwtToken(accessToken, refreshToken);
    }
}
