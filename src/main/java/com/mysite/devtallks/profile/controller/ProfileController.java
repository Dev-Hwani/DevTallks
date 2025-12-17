package com.mysite.devtallks.profile.controller;

import com.mysite.devtallks.common.dto.ResponseDto;
import com.mysite.devtallks.common.security.CustomUserDetails;
import com.mysite.devtallks.profile.dto.ProfileRequestDTO;
import com.mysite.devtallks.profile.dto.ProfileResponseDTO;
import com.mysite.devtallks.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/{memberId}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseDto<ProfileResponseDTO> getProfile(@PathVariable Long memberId) {
        return ResponseDto.ok(profileService.getProfileByMember(memberId));
    }

    @PutMapping
    public ResponseDto<ProfileResponseDTO> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long memberId,
            @Valid @RequestBody ProfileRequestDTO requestDTO) {
        validateOwner(user, memberId);
        return ResponseDto.ok(profileService.updateProfile(memberId, requestDTO));
    }

    @PostMapping("/image")
    public ResponseDto<ProfileResponseDTO> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long memberId,
            @Valid @RequestBody ProfileRequestDTO requestDTO) {
        validateOwner(user, memberId);
        return ResponseDto.ok(profileService.updateProfileImage(memberId, requestDTO.getImageUrl()));
    }

    private void validateOwner(CustomUserDetails user, Long memberId) {
        if (user == null || !user.getMemberId().equals(memberId)) {
            throw new org.springframework.security.access.AccessDeniedException("You can update only your own profile.");
        }
    }
}
