package com.mysite.devtallks.common.scheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * application.properties 또는 application.yml에 다음 키 사용 권장:
 *
 * app.scheduler.view-sync-fixed-delay-ms=60000
 * app.scheduler.view-sync-pattern=0 0/1 * * * *
 *
 * app.scheduler.popular-top-n=10
 * app.scheduler.popular-cache-ttl-seconds=300
 *
 * app.scheduler.token-cleanup-fixed-delay-ms=300000
 */
@Component
@ConfigurationProperties(prefix = "app.scheduler")
public class SchedulerProperties {

    /**
     * 조회수 Redis -> DB 반영 주기(밀리초)
     */
    private long viewSyncFixedDelayMs = 60_000L; // 기본 60초

    /**
     * 인기글 top N
     */
    private int popularTopN = 10;

    /**
     * 인기글 캐시 TTL (초)
     */
    private long popularCacheTtlSeconds = 300L; // 5분

    /**
     * 토큰/임시 키 정리 주기(밀리초)
     */
    private long tokenCleanupFixedDelayMs = 300_000L; // 5분

    // getters / setters
    public long getViewSyncFixedDelayMs() { return viewSyncFixedDelayMs; }
    public void setViewSyncFixedDelayMs(long viewSyncFixedDelayMs) { this.viewSyncFixedDelayMs = viewSyncFixedDelayMs; }

    public int getPopularTopN() { return popularTopN; }
    public void setPopularTopN(int popularTopN) { this.popularTopN = popularTopN; }

    public long getPopularCacheTtlSeconds() { return popularCacheTtlSeconds; }
    public void setPopularCacheTtlSeconds(long popularCacheTtlSeconds) { this.popularCacheTtlSeconds = popularCacheTtlSeconds; }

    public long getTokenCleanupFixedDelayMs() { return tokenCleanupFixedDelayMs; }
    public void setTokenCleanupFixedDelayMs(long tokenCleanupFixedDelayMs) { this.tokenCleanupFixedDelayMs = tokenCleanupFixedDelayMs; }
}
