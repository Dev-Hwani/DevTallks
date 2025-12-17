package com.mysite.devtallks.article.service;

import com.mysite.devtallks.article.config.ArticleViewCacheProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

@Service
public class ArticleViewCacheService {

    private static final String VIEW_KEY_PREFIX = "article:view:";
    private static final String POPULAR_KEY = "article:popular";

    private final StringRedisTemplate stringRedisTemplate;
    private final ArticleViewCacheProperties properties;

    public ArticleViewCacheService(StringRedisTemplate stringRedisTemplate,
                                   ArticleViewCacheProperties properties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.properties = properties;
    }

    public long incrementAndGet(Long articleId, int baseViewCount) {
        Long delta = stringRedisTemplate.opsForValue().increment(viewKey(articleId), 1);
        stringRedisTemplate.opsForZSet().incrementScore(POPULAR_KEY, articleId.toString(), 1);
        stringRedisTemplate.expire(viewKey(articleId), properties.getViewTtl().getSeconds(), TimeUnit.SECONDS);
        stringRedisTemplate.expire(POPULAR_KEY, properties.getPopularTtl().getSeconds(), TimeUnit.SECONDS);
        return baseViewCount + (delta == null ? 0 : delta);
    }

    public long getTotalViewCount(Long articleId, int baseViewCount) {
        String cached = stringRedisTemplate.opsForValue().get(viewKey(articleId));
        long delta = cached == null ? 0 : Long.parseLong(cached);
        return baseViewCount + delta;
    }

    public List<Long> getPopularArticleIds(int limit) {
        Set<ZSetOperations.TypedTuple<String>> ids = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(POPULAR_KEY, 0, limit - 1);
        List<Long> result = new ArrayList<>();
        if (ids != null) {
            for (ZSetOperations.TypedTuple<String> tuple : ids) {
                if (tuple.getValue() != null) {
                    result.add(Long.parseLong(tuple.getValue()));
                }
            }
        }
        return result;
    }

    public void evict(Long articleId) {
        stringRedisTemplate.delete(viewKey(articleId));
        stringRedisTemplate.opsForZSet().remove(POPULAR_KEY, articleId.toString());
    }

    public Map<Long, Long> drainViewDeltas() {
        Set<String> keys = scanKeys(VIEW_KEY_PREFIX + "*");
        Map<Long, Long> deltas = new HashMap<>();
        if (keys.isEmpty()) {
            return deltas;
        }

        for (String key : keys) {
            String value = stringRedisTemplate.opsForValue().get(key);
            if (value != null) {
                try {
                    Long delta = Long.parseLong(value);
                    Long articleId = extractArticleId(key);
                    if (articleId != null) {
                        deltas.put(articleId, delta);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        stringRedisTemplate.delete(keys);
        return deltas;
    }

    public void trimPopular(int limit) {
        Long size = stringRedisTemplate.opsForZSet().size(POPULAR_KEY);
        if (size != null && size > limit) {
            stringRedisTemplate.opsForZSet().removeRange(POPULAR_KEY, limit, size);
        }
        stringRedisTemplate.expire(POPULAR_KEY, properties.getPopularTtl().getSeconds(), TimeUnit.SECONDS);
    }

    private Long extractArticleId(String key) {
        if (key != null && key.startsWith(VIEW_KEY_PREFIX)) {
            try {
                return Long.parseLong(key.substring(VIEW_KEY_PREFIX.length()));
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        try (Cursor<byte[]> cursor = stringRedisTemplate.getConnectionFactory()
                .getConnection()
                .scan(ScanOptions.scanOptions().match(pattern).count(1000).build())) {
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
        } catch (Exception ignored) {
        }
        return keys;
    }

    private String viewKey(Long articleId) {
        return VIEW_KEY_PREFIX + articleId;
    }
}
