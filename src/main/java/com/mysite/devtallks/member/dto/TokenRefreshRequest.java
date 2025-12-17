package com.mysite.devtallks.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenRefreshRequest {

    @NotBlank(message = "refreshToken is required.")
    private String refreshToken;
}
