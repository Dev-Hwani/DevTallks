package com.mysite.devtallks.profile.controller;

import com.mysite.devtallks.profile.dto.ProfileRequestDTO;
import com.mysite.devtallks.profile.dto.ProfileResponseDTO;
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
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable Long memberId) {
        ProfileResponseDTO response = profileService.getProfileByMember(memberId);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 수정 (bio)
     */
    @PutMapping
    public ResponseEntity<ProfileResponseDTO> updateProfile(
            @PathVariable Long memberId,
            @RequestBody ProfileRequestDTO requestDTO) {

        ProfileResponseDTO response = profileService.updateProfile(memberId, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 이미지 수정
     */
    @PostMapping("/image")
    public ResponseEntity<ProfileResponseDTO> updateProfileImage(
            @PathVariable Long memberId,
            @RequestBody ProfileRequestDTO requestDTO) {

        ProfileResponseDTO response = profileService.updateProfileImage(memberId, requestDTO.getImageUrl());
        return ResponseEntity.ok(response);
    }
}
