package com.example.OAuth2Jwt.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash("refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String id;

    private String memberId;

    @Indexed
    private String token;

    @TimeToLive
    private Long expiration;

    public RefreshToken(Long memberId, String token) {
        this.id = UUID.randomUUID().toString();
        this.memberId = String.valueOf(memberId);
        this.token = token;
        this.expiration = 60 * 60 *24L;
    }
}
