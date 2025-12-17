package com.mysite.devtallks.member.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
