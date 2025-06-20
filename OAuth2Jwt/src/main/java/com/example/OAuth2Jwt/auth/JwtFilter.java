package com.example.OAuth2Jwt.auth;

import com.example.OAuth2Jwt.common.exception.ErrorResponse;
import com.example.OAuth2Jwt.common.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Map<String, List<String>> WHITE_LIST = Map.of(
            "/oauth2/**", List.of("GET"),
            "/login/**", List.of("POST", "GET"),
            "/", List.of("GET"),
            "/member/logout/**", List.of("POST"),
            "/reissue", List.of("GET")
    );

    // OAuth2 필터가 Jwt필터보다 뒤에 위치하므로 필터를 지나갈 경로들을 허용해줘야함
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return WHITE_LIST.entrySet().stream().anyMatch(entry ->
                pathMatcher.match(entry.getKey(), path) && entry.getValue().contains(method)
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        try {

            Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
            SecurityContextHolder.getContext().setAuthentication(authentication);           // 시큐리티 컨텍스트에 유저 인증정보를 저장
            filterChain.doFilter(request, response);                                        // 다음 필터로

        } catch (ExpiredJwtException e) {
            sendError(response, request, AuthException.ACCESS_TOKEN_EXPIRED);
        } catch (MalformedJwtException | SecurityException e) {
            sendError(response, request, AuthException.TOKEN_MALFORMED);
        } catch (UnsupportedJwtException e) {
            sendError(response, request, AuthException.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            sendError(response, request, AuthException.ACCESS_TOKEN_ILLEGAL);
        } catch (JwtException e) {
            sendError(response, request, AuthException.ACCESS_TOKEN_INVALID);
        }
    }

    private void sendError(HttpServletResponse response, HttpServletRequest request, ExceptionCode exceptionCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
