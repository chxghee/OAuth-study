package com.example.OAuth2Jwt.auth.presentation;

import com.example.OAuth2Jwt.auth.AuthException;
import com.example.OAuth2Jwt.common.exception.ErrorResponse;
import com.example.OAuth2Jwt.common.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CustomOAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("로그인 실패");

        sendError(response, request, AuthException.OAUTH2_LOGIN_FAILED);
    }

    private void sendError(HttpServletResponse response, HttpServletRequest request, ExceptionCode exceptionCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
