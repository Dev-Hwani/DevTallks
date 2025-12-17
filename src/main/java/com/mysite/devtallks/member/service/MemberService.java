package com.mysite.devtallks.member.service;

import com.mysite.devtallks.common.jwt.JwtTokenProvider;
import com.mysite.devtallks.common.jwt.AccessTokenBlacklistService;
import com.mysite.devtallks.common.redis.RefreshTokenService;
import com.mysite.devtallks.member.dto.AuthResponseDTO;
import com.mysite.devtallks.member.dto.MemberRequestDTO;
import com.mysite.devtallks.member.dto.MemberResponseDTO;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenBlacklistService accessTokenBlacklistService;

    private MemberResponseDTO toDTO(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    @Transactional
    public AuthResponseDTO signUp(MemberRequestDTO.SignUp request) {
        validateDuplicate(request.getEmail(), request.getNickname());

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Member saved = memberRepository.save(member);
        return buildAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(MemberRequestDTO.Login request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        return buildAuthResponse(member);
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return;
        }
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        refreshTokenService.delete(email);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String jti = jwtTokenProvider.getTokenId(accessToken);
            long ttl = jwtTokenProvider.getRemainingValidityMillis(accessToken);
            if (ttl > 0) {
                accessTokenBlacklistService.blacklist(jti, ttl);
            }
        }
    }

    @Transactional
    public AuthResponseDTO refreshTokens(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        if (!refreshTokenService.matches(email, refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        return buildAuthResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member does not exist."));
        return toDTO(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponseDTO updateMember(Long memberId, MemberRequestDTO.UpdateInfo request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member does not exist."));

        if (request.getName() != null) {
            member.setName(request.getName());
        }
        if (request.getNickname() != null) {
            if (!member.getNickname().equals(request.getNickname()) && memberRepository.existsByNickname(request.getNickname())) {
                throw new IllegalArgumentException("Nickname already in use.");
            }
            member.setNickname(request.getNickname());
        }
        if (request.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        member.setUpdatedAt(LocalDateTime.now());

        Member saved = memberRepository.save(member);
        return toDTO(saved);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    private void validateDuplicate(String email, String nickname) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("Nickname already in use.");
        }
    }

    private AuthResponseDTO buildAuthResponse(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        refreshTokenService.save(member.getEmail(), refreshToken, jwtTokenProvider.getRefreshTokenExpirationMillis());

        return AuthResponseDTO.builder()
                .member(toDTO(member))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
