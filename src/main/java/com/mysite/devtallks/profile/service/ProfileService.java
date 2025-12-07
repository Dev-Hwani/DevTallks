package com.mysite.devtallks.profile.service;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
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

    @Transactional
    public Profile createProfile(Long memberId, String bio, String imageUrl) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Profile profile = Profile.builder()
                .member(member)
                .bio(bio)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile updateProfile(Long memberId, String bio) {
        Profile profile = getProfile(memberId);
        profile.setBio(bio);
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Profile getProfileByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return profileRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));
    }
    
    @Transactional(readOnly = true)
    public Profile getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return profileRepository.findByMember(member)
                .orElse(Profile.builder()
                        .member(member)
                        .bio("")
                        .imageUrl(null)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
    }
    
    @Transactional
    public Profile updateProfileImage(Long memberId, String imageUrl) {
        Profile profile = getProfile(memberId);
        profile.setImageUrl(imageUrl);
        profile.setUpdatedAt(LocalDateTime.now());
        return profileRepository.save(profile);
    }
}
