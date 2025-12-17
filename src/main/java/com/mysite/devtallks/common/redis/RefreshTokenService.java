package com.mysite.devtallks.common.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private static final String KEY_PREFIX = "auth:refresh:";

    private final StringRedisTemplate redisTemplate;

    public RefreshTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String email, String refreshToken, long ttlMillis) {
        redisTemplate.opsForValue().set(key(email), refreshToken, Duration.ofMillis(ttlMillis));
    }

    public Optional<String> get(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key(email)));
    }

    public boolean matches(String email, String refreshToken) {
        return get(email).map(stored -> stored.equals(refreshToken)).orElse(false);
    }

    public void delete(String email) {
        redisTemplate.delete(key(email));
    }

    private String key(String email) {
        return KEY_PREFIX + email;
    }
}
