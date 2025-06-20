package com.example.OAuth2Jwt.member.domain;

import com.example.OAuth2Jwt.auth.AuthProvider;
import com.example.OAuth2Jwt.auth.application.command.OAuth2UserInfoCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(nullable = false)
    private String providerId;

    public Member(OAuth2UserInfoCommand userInfo) {
        this.email = userInfo.getEmail();
        this.nickname = userInfo.getNickname();
        this.role = Role.USER;
        this.provider = userInfo.getProvider();
        this.providerId = userInfo.getProviderId();
    }

    public void update(OAuth2UserInfoCommand userInfo) {
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.providerId = userInfo.getProviderId();
        this.provider = userInfo.getProvider();
    }
}
