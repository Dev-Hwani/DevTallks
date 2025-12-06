package com.mysite.devtallks.member.entity;

import com.mysite.devtallks.member.constant.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "members",
        indexes = {
                @Index(name = "idx_member_username", columnList = "username"),
                @Index(name = "idx_member_email", columnList = "email")
        }
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자명 */
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    /** 이메일 */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** 비밀번호(암호화 저장) */
    @Column(nullable = false)
    private String password;

    /** 회원 닉네임 */
    @Column(nullable = false, length = 30)
    private String nickname;

    /** 권한 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    /** 가입일 */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** 최근 수정일 */
    private LocalDateTime updatedAt;

    /**
     * 가입 회원 생성 로직
     */
    public static Member create(String username, String email, String password, String nickname, MemberRole role, PasswordEncoder encoder) {
        return Member.builder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .nickname(nickname)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 회원 정보 수정
     */
    public void update(String nickname) {
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String newPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(newPassword);
        this.updatedAt = LocalDateTime.now();
    }
}
