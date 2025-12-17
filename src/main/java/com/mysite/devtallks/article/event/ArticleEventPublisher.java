package com.mysite.devtallks.article.event;

import com.mysite.devtallks.article.dto.ArticleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishViewUpdated(Long articleId, long viewCount) {
        ArticleViewEvent event = ArticleViewEvent.builder()
                .articleId(articleId)
                .viewCount(viewCount)
                .type("VIEW_UPDATED")
                .build();
        messagingTemplate.convertAndSend("/topic/articles/" + articleId + "/views", event);
    }

    public void publishPopularUpdated(List<ArticleResponseDTO> popular) {
        PopularArticlesEvent event = PopularArticlesEvent.builder()
                .type("POPULAR_UPDATED")
                .articles(popular)
                .build();
        messagingTemplate.convertAndSend("/topic/articles/popular", event);
    }
}
