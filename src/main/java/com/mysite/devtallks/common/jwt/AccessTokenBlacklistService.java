package com.mysite.devtallks.common.jwt;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class AccessTokenBlacklistService {

    private static final String KEY_PREFIX = "auth:blacklist:";

    private final StringRedisTemplate redisTemplate;

    public AccessTokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklist(String jti, long ttlMillis) {
        if (jti == null) return;
        redisTemplate.opsForValue().set(key(jti), "1", Duration.ofMillis(ttlMillis));
    }

    public boolean isBlacklisted(String jti) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key(jti))).isPresent();
    }

    private String key(String jti) {
        return KEY_PREFIX + jti;
    }
}
