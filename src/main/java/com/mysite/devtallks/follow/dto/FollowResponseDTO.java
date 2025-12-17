package com.mysite.devtallks.follow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDTO {

    private Long memberId;
    private String memberNickname;
    private String memberProfileImage; // 프로필 이미지 URL
}
