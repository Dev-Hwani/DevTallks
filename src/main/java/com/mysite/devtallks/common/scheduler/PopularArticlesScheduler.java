package com.mysite.devtallks.common.scheduler;

import com.mysite.devtallks.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * DB 기반 인기글 집계 스케줄러
 * - 작은 프로젝트에서는 DB에서 직접 정렬/조회 후 캐싱하는 방식이 가장 간단함
 * - 대규모 트래픽이면 Redis Sorted Set을 사용해 실시간 집계하는 것이 더 효율적
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PopularArticlesScheduler {

    private final SchedulerProperties props;
    private final com.mysite.devtallks.article.repository.ArticleRepository articleRepository; // 프로젝트에 맞게 구현 필요
    private final RedisService redisService;

    /**
     * 5분마다 집계(예), 필요시 cron 또는 fixedDelay 변경
     */
    @Scheduled(fixedDelayString = "${app.scheduler.popular-cache-ttl-seconds:300000}")
    public void refreshPopularArticles() {
        try {
            int topN = props.getPopularTopN();

            // Article 엔티티에서 DTO로 변환해서 캐시 저장하는 것이 좋음.
            List<com.mysite.devtallks.article.dto.ArticleSummaryDto> topList =
                    articleRepository.findTopNByOrderByViewCountDesc(topN);

            // 캐시에 저장 (TTL)
            redisService.setObject("popular:articles", topList, Duration.ofSeconds(props.getPopularCacheTtlSeconds()));
            log.info("Popular articles cached, size={}", topList == null ? 0 : topList.size());
        } catch (Exception ex) {
            log.error("PopularArticlesScheduler failure: {}", ex.getMessage(), ex);
        }
    }
}
