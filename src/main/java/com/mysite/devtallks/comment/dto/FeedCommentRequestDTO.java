package com.mysite.devtallks.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedCommentRequestDTO {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Long parentId; // 대댓글 기능
}
