package com.mysite.devtallks.article.event;

import com.mysite.devtallks.article.dto.ArticleResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularArticlesEvent {
    private String type; // POPULAR_UPDATED
    private List<ArticleResponseDTO> articles;
}
