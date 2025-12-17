package com.mysite.devtallks.feed.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponseDTO {

    private Long id;
    private Long memberId;
    private String memberNickname;
    private String content;
    private String imageUrl;
    private long likeCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<FeedCommentResponseDTO> comments; // 대댓글 포함 가능
}
