package com.mysite.devtallks.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 간단하고 안전한 분산 락 서비스
 *
 * 사용 방법:
 * String token = redisLockService.tryLock("myKey", Duration.ofSeconds(10));
 * if (token == null) { // 락 획득 실패 }
 * // 작업 수행...
 * redisLockService.releaseLock("myKey", token);
 *
 * 구현 원리:
 * - 락 획득: SET key value NX PX ttl (StringRedisTemplate.opsForValue().setIfAbsent + expire)
 * - 락 해제: Lua 스크립트로 value 비교 후 삭제 (원자적으로 수행)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    private static final String RELEASE_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "   return redis.call('del', KEYS[1]) " +
            "else " +
            "   return 0 " +
            "end";

    /**
     * 락 시도: 성공하면 토큰(UUID) 반환, 실패하면 null 반환
     */
    public String tryLock(String key, Duration ttl) {
        String token = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, token, ttl);
        if (Boolean.TRUE.equals(success)) {
            return token;
        }
        return null;
    }

    /**
     * 안전한 락 해제: 토큰이 같을 때만 삭제 (Lua 스크립트 사용)
     */
    public boolean releaseLock(String key, String token) {
        Object result = redisTemplate.execute((connection) ->
                connection.eval(RELEASE_SCRIPT.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        key.getBytes(),
                        token.getBytes()), true);
        if (result instanceof Long) {
            return ((Long) result) > 0;
        }
        return false;
    }

    /**
     * 블로킹 방식: ttl 내에서 주기적으로 시도하여 락을 얻음 (사용시 주의)
     */
    public String lockBlocking(String key, Duration ttl, Duration waitTimeout, Duration retryInterval) throws InterruptedException {
        long waitMillis = waitTimeout.toMillis();
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < waitMillis) {
            String token = tryLock(key, ttl);
            if (token != null) return token;
            Thread.sleep(retryInterval.toMillis());
        }
        return null;
    }
}
