package com.mysite.devtallks.article.service;

import com.mysite.devtallks.article.config.ArticleViewCacheProperties;
import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.repository.ArticleRepository;
import com.mysite.devtallks.article.event.ArticleEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArticleViewSyncScheduler {

    private final ArticleViewCacheService articleViewCacheService;
    private final ArticleRepository articleRepository;
    private final ArticleViewCacheProperties properties;
    private final ArticleService articleService;
    private final ArticleEventPublisher articleEventPublisher;

    /**
     * Flush view deltas from Redis to DB and trim the popularity leaderboard.
     * Runs on configurable interval.
     */
    @Scheduled(fixedDelayString = "#{@articleViewCacheProperties.flushInterval.toMillis()}")
    public void flushViews() {
        Map<Long, Long> deltas = articleViewCacheService.drainViewDeltas();
        if (deltas.isEmpty()) {
            return;
        }

        List<Article> toSave = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : deltas.entrySet()) {
            Optional<Article> optionalArticle = articleRepository.findById(entry.getKey());
            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                long newCount = article.getViewCount() + entry.getValue();
                article.setViewCount(Math.toIntExact(newCount));
                toSave.add(article);
            } else {
                // Clean up if article no longer exists.
                articleViewCacheService.evict(entry.getKey());
            }
        }

        if (!toSave.isEmpty()) {
            articleRepository.saveAll(toSave);
        }

        // Prevent unbounded growth of popularity leaderboard.
        articleViewCacheService.trimPopular(properties.getPopularTrimLimit());

        // Broadcast latest popular list to subscribers.
        articleEventPublisher.publishPopularUpdated(
                articleService.getPopularArticles(properties.getPopularTrimLimit())
        );
    }
}
