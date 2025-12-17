package com.mysite.devtallks.member.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberRequestDTO {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class SignUp {

	    @Email
	    @NotBlank
	    private String email;

	    @NotBlank
	    @Size(min = 2, max = 20)
	    private String name;

	    @NotBlank
	    @Size(min = 2, max = 20)
	    private String nickname;

	    @NotBlank
	    @Size(min = 8, max = 100)
	    @Pattern(
	        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
	        message = "비밀번호는 최소 8자 이상, 대문자/소문자/숫자/특수문자를 모두 포함해야 합니다."
	    )
	    private String password;
	}

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateInfo {

        @Size(min = 2, max = 20)
        private String name;

        @Size(min = 2, max = 20)
        private String nickname;

        @Size(min = 8, max = 100)
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "비밀번호는 최소 8자 이상, 대문자/소문자/숫자/특수문자를 모두 포함해야 합니다."
        )
        private String password;
    }
}
