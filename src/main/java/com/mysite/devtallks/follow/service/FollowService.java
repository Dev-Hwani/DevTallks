package com.mysite.devtallks.follow.service;

import com.mysite.devtallks.follow.dto.FollowResponseDTO;
import com.mysite.devtallks.follow.entity.Follow;
import com.mysite.devtallks.follow.repository.FollowRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import com.mysite.devtallks.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    // ===========================
    // 팔로우 등록
    // ===========================
    @Transactional
    public FollowResponseDTO follow(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워 회원이 존재하지 않습니다."));

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 회원이 존재하지 않습니다."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseGet(() -> followRepository.save(
                        Follow.builder()
                                .follower(follower)
                                .following(following)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));

        return mapToDTO(follow.getFollowing());
    }

    // ===========================
    // 언팔로우
    // ===========================
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워 회원이 존재하지 않습니다."));

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 회원이 존재하지 않습니다."));

        followRepository.findByFollowerAndFollowing(follower, following)
                .ifPresent(followRepository::delete);
    }

    // ===========================
    // 회원의 팔로워 목록 DTO로 반환
    // ===========================
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowersDTO(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Page<Follow> followPage = followRepository.findByFollowing(member, pageable);

        return followPage.map(f -> mapToDTO(f.getFollower()));
    }

    // ===========================
    // 회원의 팔로잉 목록 DTO로 반환
    // ===========================
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowingsDTO(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Page<Follow> followPage = followRepository.findByFollower(member, pageable);

        return followPage.map(f -> mapToDTO(f.getFollowing()));
    }

    // ===========================
    // Member -> FollowResponseDTO 변환
    // ===========================
    private FollowResponseDTO mapToDTO(Member member) {

        String profileImage = null;

        // 최신 프로필 1개만 선택
        if (member.getProfiles() != null && !member.getProfiles().isEmpty()) {
            Profile latestProfile = member.getProfiles()
                    .stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .findFirst()
                    .orElse(null);

            if (latestProfile != null) {
                profileImage = latestProfile.getImageUrl();
            }
        }

        return FollowResponseDTO.builder()
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .memberProfileImage(profileImage)  // 최신 프로필 이미지 URL
                .build();
    }
}
