package com.example.OAuth2Jwt.auth;

import com.example.OAuth2Jwt.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthException implements ExceptionCode {

    OAUTH2_LOGIN_FAILED("로그인 실패", HttpStatus.UNAUTHORIZED, "아이디와 패스워드가 일치하지 않습니다."),
    PERMISSION_DENIED("권한 없음", HttpStatus.FORBIDDEN, "해당 요청을 처리할 권한이 없습니다."),

    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰 없음", HttpStatus.BAD_REQUEST, "리프레시 토큰이 Redis에 존재하지 않습니다."),

    TOKEN_MALFORMED("잘못된 JWT 서명", HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    TOKEN_UNSUPPORTED("지원하지 않는 JWT 토큰", HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),

    REFRESH_TOKEN_COOKIE_NOT_FOUND("리프레시 토큰 없음", HttpStatus.UNAUTHORIZED, "리프레시 토큰이 쿠키에 존재하지 않습니다."),
    REFRESH_TOKEN_INVALID("억세스 토큰 인증 실패", HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED("리프레시 토큰 만료", HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_ILLEGAL("리프레시 토큰 형식 오류", HttpStatus.BAD_REQUEST, "리프레시 토큰이 비어있거나 형식이 잘못되었습니다."),

    ACCESS_TOKEN_INVALID("억세스 토큰 인증 실패", HttpStatus.UNAUTHORIZED, "억세스 토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_EXPIRED("억세스 토큰 만료", HttpStatus.UNAUTHORIZED, "억세스 토큰이 만료되었습니다."),
    ACCESS_TOKEN_ILLEGAL("억세스 토큰 형식 오류", HttpStatus.BAD_REQUEST, "억세스 토큰이 비어있거나 형식이 잘못되었습니다.");

    private final String title;
    private final HttpStatus httpStatus;
    private final String detail;
}
