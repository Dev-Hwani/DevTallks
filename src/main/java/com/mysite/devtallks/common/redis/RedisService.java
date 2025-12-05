package com.mysite.devtallks.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 공통 Redis 서비스
 * - StringRedisTemplate 기반 메서드 (문자열 단위) 및 RedisTemplate(Object) 지원
 * - 제네릭한 get/set, incr, expire, delete, hash ops 등 자주 쓰이는 연산 제공
 *
 * 사용 예:
 * redisService.set("key", value, Duration.ofMinutes(5));
 * MyDto dto = redisService.get("key", MyDto.class);
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    // ---------------- 기본 문자열 연산 ----------------

    public void set(String key, String value, @Nullable Duration ttl) {
        if (ttl == null) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, ttl);
        }
    }

    @Nullable
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean exists(String key) {
        Boolean b = stringRedisTemplate.hasKey(key);
        return b != null && b;
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public long increment(String key, long delta, @Nullable Duration ttlIfNew) {
        Long value = stringRedisTemplate.opsForValue().increment(key, delta);
        if (value != null && value == delta && ttlIfNew != null) {
            // 새로 생성된 경우(=delta만큼 증가한 경우)에 TTL 설정
            stringRedisTemplate.expire(key, ttlIfNew);
        }
        return value == null ? 0L : value;
    }

    public void expire(String key, Duration ttl) {
        stringRedisTemplate.expire(key, ttl);
    }

    // ---------------- 객체(복합 타입) 연산 ----------------

    /**
     * 객체 저장 (RedisTemplate + JSON 직렬화)
     */
    public void setObject(String key, Object value, @Nullable Duration ttl) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        if (ttl == null) {
            ops.set(key, value);
        } else {
            ops.set(key, value, ttl);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getObject(String key, Class<T> clazz) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) return null;
        // 캐스팅은 호출자가 책임
        return (T) o;
    }

    // ---------------- Hash 연산 ----------------
    public void putHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getHash(String key, String hashKey, Class<T> clazz) {
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        return (T) o;
    }

    public Map<Object, Object> getAllHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // ---------------- 기타 유틸 ----------------
    public Long getExpire(String key, TimeUnit unit) {
        return stringRedisTemplate.getExpire(key, unit);
    }
}
