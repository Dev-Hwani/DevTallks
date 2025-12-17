package com.mysite.devtallks.feed.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedRequestDTO {

    @NotBlank(message = "피드 내용을 입력해주세요.")
    private String content;

    private String imageUrl; // 선택적 이미지
}
