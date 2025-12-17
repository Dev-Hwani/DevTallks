package com.mysite.devtallks.comment.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedCommentResponseDTO {

    private Long id;
    private Long feedId;
    private Long memberId;
    private String memberNickname;
    private String content;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
