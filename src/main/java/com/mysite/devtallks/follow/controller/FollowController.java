package com.mysite.devtallks.follow.controller;

import com.mysite.devtallks.follow.dto.FollowResponseDTO;
import com.mysite.devtallks.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // ===========================
    // 팔로우
    // ===========================
    @PostMapping("/{followerId}/{followingId}")
    public ResponseEntity<FollowResponseDTO> follow(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        FollowResponseDTO dto = followService.follow(followerId, followingId);
        return ResponseEntity.ok(dto);
    }

    // ===========================
    // 언팔로우
    // ===========================
    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<Void> unfollow(
            @PathVariable Long followerId,
            @PathVariable Long followingId
    ) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    // ===========================
    // 팔로워 목록
    // ===========================
    @GetMapping("/followers/{memberId}")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowers(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                followService.getFollowersDTO(memberId, pageable)
        );
    }

    // ===========================
    // 팔로잉 목록
    // ===========================
    @GetMapping("/followings/{memberId}")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowings(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                followService.getFollowingsDTO(memberId, pageable)
        );
    }
}
