package com.mysite.devtallks.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequestDTO {

    @Size(max = 200, message = "프로필 소개는 200자 이하로 작성해야 합니다.")
    private String bio;

    private String imageUrl; // 프로필 이미지 URL
}
