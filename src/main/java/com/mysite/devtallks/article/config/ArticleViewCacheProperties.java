package com.mysite.devtallks.article.config;

import java.time.Duration;

public class ArticleViewCacheProperties {

    /**
     * TTL for per-article view delta keys.
     */
    private Duration viewTtl = Duration.ofHours(24);

    /**
     * TTL for popularity leaderboard.
     */
    private Duration popularTtl = Duration.ofHours(24);

    /**
     * Max number of items kept in popularity leaderboard.
     */
    private int popularTrimLimit = 500;

    /**
     * Flush interval for syncing Redis deltas to DB.
     */
    private Duration flushInterval = Duration.ofMinutes(5);

    public Duration getViewTtl() {
        return viewTtl;
    }

    public void setViewTtl(Duration viewTtl) {
        this.viewTtl = viewTtl;
    }

    public Duration getPopularTtl() {
        return popularTtl;
    }

    public void setPopularTtl(Duration popularTtl) {
        this.popularTtl = popularTtl;
    }

    public int getPopularTrimLimit() {
        return popularTrimLimit;
    }

    public void setPopularTrimLimit(int popularTrimLimit) {
        this.popularTrimLimit = popularTrimLimit;
    }

    public Duration getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(Duration flushInterval) {
        this.flushInterval = flushInterval;
    }
}
