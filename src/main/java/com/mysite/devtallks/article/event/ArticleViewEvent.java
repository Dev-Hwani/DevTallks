package com.mysite.devtallks.article.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleViewEvent {
    private Long articleId;
    private long viewCount;
    private String type; // VIEW_UPDATED
}
