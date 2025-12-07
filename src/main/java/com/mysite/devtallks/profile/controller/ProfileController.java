package com.mysite.devtallks.profile.controller;

import com.mysite.devtallks.profile.entity.Profile;
import com.mysite.devtallks.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/{memberId}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 조회
     */
    @GetMapping
    public ResponseEntity<Profile> getProfile(@PathVariable Long memberId) {
        Profile profile = profileService.getProfile(memberId);
        return ResponseEntity.ok(profile);
    }

    /**
     * 프로필 수정 (bio)
     */
    @PutMapping
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long memberId,
            @RequestParam String bio) {

        Profile updatedProfile = profileService.updateProfile(memberId, bio);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * 프로필 이미지 업로드/수정
     */
    @PostMapping("/image")
    public ResponseEntity<Profile> updateProfileImage(
            @PathVariable Long memberId,
            @RequestParam String imageUrl) {

        Profile updatedProfile = profileService.updateProfileImage(memberId, imageUrl);
        return ResponseEntity.ok(updatedProfile);
    }
}

