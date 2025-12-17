package com.mysite.devtallks.profile.service;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import com.mysite.devtallks.profile.dto.ProfileRequestDTO;
import com.mysite.devtallks.profile.dto.ProfileResponseDTO;
import com.mysite.devtallks.profile.entity.Profile;
import com.mysite.devtallks.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    // ==========================================
    // 프로필 생성
    // ==========================================
    @Transactional
    public ProfileResponseDTO createProfile(Long memberId, ProfileRequestDTO dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Profile profile = Profile.builder()
                .member(member)
                .bio(dto.getBio())
                .imageUrl(dto.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
        return toDTO(profile);
    }

    // ==========================================
    // 프로필 정보 수정 (bio)
    // ==========================================
    @Transactional
    public ProfileResponseDTO updateProfile(Long memberId, ProfileRequestDTO dto) {

        Profile profile = getLatestProfile(memberId);
        profile.setBio(dto.getBio());
        profile.setUpdatedAt(LocalDateTime.now());

        profileRepository.save(profile);
        return toDTO(profile);
    }

    // ==========================================
    // 프로필 이미지 수정
    // ==========================================
    @Transactional
    public ProfileResponseDTO updateProfileImage(Long memberId, String imageUrl) {

        Profile profile = getLatestProfile(memberId);
        profile.setImageUrl(imageUrl);
        profile.setUpdatedAt(LocalDateTime.now());

        profileRepository.save(profile);
        return toDTO(profile);
    }

    // ==========================================
    // 특정 회원 프로필 조회
    // ==========================================
    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileByMember(Long memberId) {
        return toDTO(getLatestProfile(memberId));
    }

    // ==========================================
    // 가장 최신 프로필 1개 가져오기
    // ==========================================
    private Profile getLatestProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return profileRepository.findTopByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));
    }

    // ==========================================
    // 엔티티 → DTO 변환
    // ==========================================
    private ProfileResponseDTO toDTO(Profile profile) {

        return ProfileResponseDTO.builder()
                .memberId(profile.getMember().getId())
                .nickname(profile.getMember().getNickname())
                .bio(profile.getBio())
                .imageUrl(profile.getImageUrl())
                .build();
    }
}
