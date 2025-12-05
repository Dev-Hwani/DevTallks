package com.mysite.devtallks.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Redis에 쌓이는 임시 키(토큰 블랙리스트, 임시 업로드 정보 등)를 주기적으로 정리
 * - 안전을 위해 패턴목록은 최소화해서 운영
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {

    private final SchedulerProperties props;
    private final StringRedisTemplate stringRedisTemplate;

    // 정리할 패턴 목록 (필요시 application.properties로 외부화)
    private static final String[] CLEANUP_PATTERNS = new String[]{
            "blacklist:token:*",
            "tmp:*",
            "upload:tmp:*"
    };

    @Scheduled(fixedDelayString = "${app.scheduler.token-cleanup-fixed-delay-ms:300000}")
    public void cleanup() {
        try {
            for (String pattern : CLEANUP_PATTERNS) {
                Set<String> keys = stringRedisTemplate.keys(pattern);
                if (keys == null || keys.isEmpty()) continue;
                log.info("TokenCleanupScheduler will delete {} keys for pattern={}", keys.size(), pattern);
                stringRedisTemplate.delete(keys);
            }
        } catch (Exception ex) {
            log.error("TokenCleanupScheduler failure: {}", ex.getMessage(), ex);
        }
    }
}
