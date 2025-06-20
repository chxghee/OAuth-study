package com.example.OAuth2Jwt.member.application;

import com.example.OAuth2Jwt.auth.AuthException;
import com.example.OAuth2Jwt.auth.JwtTokenProvider;
import com.example.OAuth2Jwt.auth.domain.RefreshToken;
import com.example.OAuth2Jwt.auth.domain.RefreshTokenRepository;
import com.example.OAuth2Jwt.common.exception.ApplicationException;
import com.example.OAuth2Jwt.member.domain.MemberRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void logout(String refreshToken) {

        try {
            Claims claims = jwtTokenProvider.getClaims(refreshToken);

            validateTokenCategory(claims);

            RefreshToken refreshTokenFromRepository = getRefreshTokenFromRepository(refreshToken);

            // 로그아웃 진행
            // Refresh 토큰 DB에서 제거
            refreshTokenRepository.delete(refreshTokenFromRepository);

        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthException.REFRESH_TOKEN_EXPIRED);
        } catch (MalformedJwtException | SecurityException e) {
            throw new ApplicationException(AuthException.TOKEN_MALFORMED);
        } catch (UnsupportedJwtException e) {
            throw new ApplicationException(AuthException.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthException.REFRESH_TOKEN_ILLEGAL);
        } catch (JwtException e) {
            throw new ApplicationException(AuthException.REFRESH_TOKEN_INVALID);
        }
    }

    private static void validateTokenCategory(Claims claims) {
        String category = claims.get("category", String.class);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException();
        }
    }

    private RefreshToken getRefreshTokenFromRepository(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(AuthException.REFRESH_TOKEN_NOT_FOUND));
    }


}
