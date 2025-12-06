package com.mysite.devtallks.member.service;

import com.mysite.devtallks.common.jwt.JwtTokenProvider;
import com.mysite.devtallks.member.constant.MemberRole;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.exception.DuplicateEmailException;
import com.mysite.devtallks.member.exception.DuplicateUsernameException;
import com.mysite.devtallks.member.exception.MemberNotFoundException;
import com.mysite.devtallks.member.repository.MemberRepository;
import com.mysite.devtallks.member.service.dto.request.MemberLoginRequest;
import com.mysite.devtallks.member.service.dto.request.MemberSignupRequest;
import com.mysite.devtallks.member.service.dto.request.MemberUpdateRequest;
import com.mysite.devtallks.member.service.dto.response.LoginResponse;
import com.mysite.devtallks.member.service.dto.response.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public MemberResponse signup(MemberSignupRequest request) {
        // 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        }
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException("이미 사용중인 사용자명입니다.");
        }

        Member member = Member.create(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                MemberRole.USER,
                passwordEncoder
        );

        Member saved = memberRepository.save(member);

        return toMemberResponse(saved);
    }

    @Override
    public LoginResponse login(MemberLoginRequest request) {
        // AuthenticationManager로 인증 시도 (BadCredentialsException 등 발생 가능)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증 성공 시 토큰 발급 (JwtTokenProvider의 메서드 이름에 맞춤)
        String accessToken = jwtTokenProvider.createAccessToken(request.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.getEmail());

        return LoginResponse.of(accessToken, refreshToken);
    }

    @Override
    public MemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다. id=" + memberId));
        return toMemberResponse(member);
    }

    @Override
    public MemberResponse findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다. username=" + username));
        return toMemberResponse(member);
    }

    @Override
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다. id=" + memberId));

        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            member.update(request.getNickname());
        }

        // 비밀번호 변경 시
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            member.changePassword(request.getNewPassword(), passwordEncoder);
        }

        Member updated = memberRepository.save(member);
        return toMemberResponse(updated);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다. id=" + memberId));
        memberRepository.delete(member);
    }

    /* ------- helper ------- */
    private MemberResponse toMemberResponse(Member m) {
        return MemberResponse.builder()
                .id(m.getId())
                .username(m.getUsername())
                .email(m.getEmail())
                .nickname(m.getNickname())
                .role(m.getRole().roleName())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}
