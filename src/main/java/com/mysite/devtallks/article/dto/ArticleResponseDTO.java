package com.mysite.devtallks.article.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String memberNickname;
    private long likeCount;
    private long commentCount;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
