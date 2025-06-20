package com.example.OAuth2Jwt.auth.application;

import com.example.OAuth2Jwt.auth.AuthException;
import com.example.OAuth2Jwt.auth.JwtTokenProvider;
import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import com.example.OAuth2Jwt.auth.application.command.JwtToken;
import com.example.OAuth2Jwt.auth.domain.RefreshToken;
import com.example.OAuth2Jwt.auth.domain.RefreshTokenRepository;
import com.example.OAuth2Jwt.common.exception.ApplicationException;
import com.example.OAuth2Jwt.member.MemberException;
import com.example.OAuth2Jwt.member.application.MemberAuthInfo;
import com.example.OAuth2Jwt.member.domain.Member;
import com.example.OAuth2Jwt.member.domain.MemberRepository;
import com.example.OAuth2Jwt.member.domain.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public JwtToken reissueToken(String refreshToken) {

        // refresh token 유효성 검사 (만료?, 카테고리가 리프레쉬 토큰인지)
        try {

            Claims claims = jwtTokenProvider.getClaims(refreshToken);   // 만료 여부 자동 검증

            validateTokenCategory(claims);

            Long memberId = Long.valueOf(claims.get("id", String.class));

            RefreshToken refreshTokenFromRepository = getRefreshTokenFromRepository(refreshToken);
            CustomOAuth2User customOAuth2User = getCustomOAuth2User(memberId);

            // 3. access token 발급
            String accessToken = jwtTokenProvider.createAccessToken(customOAuth2User);

            // 4. refresh token rotate (재발급)
            String rotatedRefreshToken = jwtTokenProvider.createRefreshToken(memberId);

            // 삭제 후 새로 발급받은 rotated refresh Token 저장
            refreshTokenRepository.delete(refreshTokenFromRepository);
            refreshTokenRepository.save(new RefreshToken(memberId, rotatedRefreshToken));

            return JwtToken.of(accessToken, rotatedRefreshToken);

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

    private CustomOAuth2User getCustomOAuth2User(Long memberId) {
        return new CustomOAuth2User(
                MemberAuthInfo.from(
                        memberRepository.findById(memberId)
                            .orElseThrow(() -> new ApplicationException(MemberException.NOT_FOUND_MEMBER))
                )
        );
    }

    // 없으면 오류
    private RefreshToken getRefreshTokenFromRepository(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApplicationException(AuthException.REFRESH_TOKEN_NOT_FOUND));
    }

    private static void validateTokenCategory(Claims claims) {
        String category = claims.get("category", String.class);
        if (!category.equals("refresh")) {
            throw new ApplicationException(AuthException.REFRESH_TOKEN_INVALID);
        }
    }
}
