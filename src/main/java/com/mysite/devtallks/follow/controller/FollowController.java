package com.mysite.devtallks.follow.controller;

import com.mysite.devtallks.follow.entity.Follow;
import com.mysite.devtallks.follow.service.FollowService;
import com.mysite.devtallks.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우 생성
     */
    @PostMapping
    public ResponseEntity<String> follow(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {

        followService.follow(followerId, followingId);
        return ResponseEntity.ok("팔로우 성공");
    }

    /**
     * 팔로우 취소
     */
    @DeleteMapping
    public ResponseEntity<String> unfollow(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {

        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok("언팔로우 성공");
    }

    /**
     * 팔로워 조회 (페이지 처리)
     */
    @GetMapping("/followers/{memberId}")
    public ResponseEntity<Page<Member>> getFollowers(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Member> followersPage = followService.getFollowers(memberId, pageable);
        return ResponseEntity.ok(followersPage);
    }

    /**
     * 팔로잉 조회 (페이지 처리)
     */
    @GetMapping("/followings/{memberId}")
    public ResponseEntity<Page<Member>> getFollowings(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Member> followingsPage = followService.getFollowings(memberId, pageable);
        return ResponseEntity.ok(followingsPage);
    }
}
