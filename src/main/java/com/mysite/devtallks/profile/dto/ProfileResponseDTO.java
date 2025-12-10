package com.mysite.devtallks.profile.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDTO {

    private Long memberId;
    private String nickname;
    private String bio;
    private String imageUrl;
}
