package com.example.OAuth2Jwt.member;

import com.example.OAuth2Jwt.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberException implements ExceptionCode {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "존재하는 아이디", "아이디가 이미 존재합니다."),
    INVALID_CREDENTIALS(HttpStatus.CONFLICT, "로그인 실패", "아이디와 패스워드가 일치하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", "해당 유저는 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;

}
