package com.mysite.devtallks.common.scheduler;

import com.mysite.devtallks.common.redis.RedisKeyUtil;
import com.mysite.devtallks.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Redis에 임시로 쌓은 조회수를 주기적으로 DB에 반영
 *
 * 주의:
 * - article repository는 프로젝트의 article 패키지에 구현되어 있어야 함
 * - 키 네이밍은 "article:view:{articleId}" 형식으로 일치해야 함
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountSyncScheduler {

    private final SchedulerProperties props;
    private final RedisService redisService;
    private final StringRedisTemplate stringRedisTemplate;
    private final com.mysite.devtallks.article.repository.ArticleRepository articleRepository; // 프로젝트에 맞게 구현 필요

    /**
     * fixedDelay 방식으로 구현 (props.getViewSyncFixedDelayMs())
     * 실행 간격은 properties로 제어 가능.
     */
    @Scheduled(fixedDelayString = "${app.scheduler.view-sync-fixed-delay-ms:60000}")
    public void syncViewCountsToDb() {
        try {
            // 패턴은 RedisKeyUtil 사용 규칙과 일치해야 함
            String pattern = "article:view:*";
            Set<String> keys = stringRedisTemplate.keys(pattern);

            if (keys == null || keys.isEmpty()) {
                return;
            }

            for (String key : keys) {
                try {
                    String countStr = stringRedisTemplate.opsForValue().get(key);
                    if (countStr == null) continue;
                    long delta = 0L;
                    try {
                        delta = Long.parseLong(countStr);
                    } catch (NumberFormatException nfe) {
                        log.warn("Invalid view count for key {} value={}", key, countStr);
                        continue;
                    }

                    // key -> articleId 추출: article:view:{id}
                    String idPart = key.substring("article:view:".length());
                    Long articleId = null;
                    try {
                        articleId = Long.parseLong(idPart);
                    } catch (NumberFormatException nfe) {
                        log.warn("Invalid article id extracted from key {} (part={})", key, idPart);
                        continue;
                    }

                    // 실제 DB 업데이트 (repository 구현체 필요)
                    articleRepository.findById(articleId).ifPresent(article -> {
                        article.setViewCount(article.getViewCount() + delta);
                        articleRepository.save(article);
                        // Redis 키 삭제 (또는 0으로 초기화)
                        stringRedisTemplate.delete(key);
                        log.info("Synced viewCount for articleId={} delta={}", articleId, delta);
                    });

                } catch (Exception e) {
                    log.error("Error syncing key {} : {}", key, e.getMessage(), e);
                }
            }
        } catch (Exception ex) {
            log.error("ViewCountSyncScheduler failure: {}", ex.getMessage(), ex);
        }
    }
}
