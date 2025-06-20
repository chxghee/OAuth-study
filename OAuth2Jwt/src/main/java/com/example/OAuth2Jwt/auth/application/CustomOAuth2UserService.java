package com.example.OAuth2Jwt.auth.application;

import com.example.OAuth2Jwt.auth.AuthProvider;
import com.example.OAuth2Jwt.auth.application.command.CustomOAuth2User;
import com.example.OAuth2Jwt.auth.application.command.OAuth2UserInfoCommand;
import com.example.OAuth2Jwt.auth.application.command.GoogleUserInfoCommand;
import com.example.OAuth2Jwt.auth.application.command.NaverUserInfoCommand;
import com.example.OAuth2Jwt.member.application.MemberAuthInfo;
import com.example.OAuth2Jwt.member.domain.Member;
import com.example.OAuth2Jwt.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth2 user: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfoCommand userInfo = getUserInfo(oAuth2User, registrationId);

        Member member = upsertMember(userInfo);
        MemberAuthInfo memberAuthInfo = MemberAuthInfo.from(member);

        return new CustomOAuth2User(memberAuthInfo);
    }

    // Provider + ProviderId 로 DB 내에 있는 member 조회 후 있는 값이면 객체 update / 없으면 새 객체 생성 후 insert
    private Member upsertMember(OAuth2UserInfoCommand userInfo) {
        return memberRepository.findByProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId())
                .map(existing -> {
                    existing.update(userInfo);
                    return existing;
                })
                .orElse(memberRepository.save(new Member(userInfo)));
    }

    private static OAuth2UserInfoCommand getUserInfo(OAuth2User oAuth2User, String registrationId) {
        if (registrationId.equals(AuthProvider.NAVER.getCode())) {
            return new NaverUserInfoCommand(oAuth2User.getAttributes());
        } else {
            return new GoogleUserInfoCommand(oAuth2User.getAttributes());
        }
    }
}
