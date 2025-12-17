package com.mysite.devtallks.follow.controller;

import com.mysite.devtallks.common.dto.ResponseDto;
import com.mysite.devtallks.follow.dto.FollowResponseDTO;
import com.mysite.devtallks.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public ResponseDto<FollowResponseDTO> follow(
            @AuthenticationPrincipal com.mysite.devtallks.common.security.CustomUserDetails user,
            @PathVariable Long followingId) {
        return ResponseDto.ok(followService.follow(user.getMemberId(), followingId));
    }

    @DeleteMapping("/{followingId}")
    public ResponseDto<Void> unfollow(
            @AuthenticationPrincipal com.mysite.devtallks.common.security.CustomUserDetails user,
            @PathVariable Long followingId) {
        followService.unfollow(user.getMemberId(), followingId);
        return ResponseDto.ok(null, "Unfollowed");
    }

    @GetMapping("/followers/{memberId}")
    public ResponseDto<Page<FollowResponseDTO>> getFollowers(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseDto.ok(followService.getFollowersDTO(memberId, pageable));
    }

    @GetMapping("/followings/{memberId}")
    public ResponseDto<Page<FollowResponseDTO>> getFollowings(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseDto.ok(followService.getFollowingsDTO(memberId, pageable));
    }
}
