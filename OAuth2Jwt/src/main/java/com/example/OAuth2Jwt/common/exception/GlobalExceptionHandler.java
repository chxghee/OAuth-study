package com.example.OAuth2Jwt.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        log.info("잡힌 예외 타입: " + e.getClass().getName());
        log.info("메세지: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("알 수 없는 오류", 500, "관리자에게 문의 하세요", request.getRequestURI()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationExceptionHandler(ApplicationException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getCode().getHttpStatus())
                .body(ErrorResponse.of(e.getCode(), request.getRequestURI()));
    }
}
