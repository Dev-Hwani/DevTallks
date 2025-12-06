package com.mysite.devtallks.member.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
