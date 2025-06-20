package com.example.OAuth2Jwt.config;

import com.example.OAuth2Jwt.auth.presentation.CustomOAuthFailureHandler;
import com.example.OAuth2Jwt.auth.presentation.CustomOAuthSuccessHandler;
import com.example.OAuth2Jwt.auth.JwtFilter;
import com.example.OAuth2Jwt.auth.JwtTokenProvider;
import com.example.OAuth2Jwt.auth.application.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;
    private final CustomOAuthFailureHandler customOAuthFailureHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)

                .cors(Customizer.withDefaults())  // 빈으로 등록된 CORS 설정 활성화

                // jwt filter 등록
                .addFilterBefore(new JwtFilter(jwtTokenProvider, objectMapper),
                        UsernamePasswordAuthenticationFilter.class)

                // OAuth2 설정
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(customOAuthSuccessHandler)
                        .failureHandler(customOAuthFailureHandler)
                )

                // 경로별 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/reissue/**", "/oauth2/**", "/login/**", "/member/logout").permitAll()
                        .anyRequest().authenticated()
                )

                // 세션 설정 - STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    // CORS 설정 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트 도메인
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);                                       // 쿠키/인증 정보 포함 허용
        config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));       // 프론트에서 해당 헤더 접근 가능하게

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 전체 경로에 적용

        return source;
    }
}
