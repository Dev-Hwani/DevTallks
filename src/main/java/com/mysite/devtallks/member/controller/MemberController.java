package com.mysite.devtallks.member.controller;

import com.mysite.devtallks.member.dto.MemberResponseDTO;
import com.mysite.devtallks.member.service.MemberService;
import com.mysite.devtallks.follow.dto.FollowResponseDTO;
import com.mysite.devtallks.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;

    // 회원 정보 조회
    @GetMapping("/{memberId}")
    public MemberResponseDTO getMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

    // 팔로워 목록 조회
    @GetMapping("/{memberId}/followers")
    public Page<FollowResponseDTO> getFollowers(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return followService.getFollowersDTO(memberId, pageable);
    }

    // 팔로잉 목록 조회 
    @GetMapping("/{memberId}/followings")
    public Page<FollowResponseDTO> getFollowings(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return followService.getFollowingsDTO(memberId, pageable); // **여기 수정됨**
    }

    // 팔로우 추가
    @PostMapping("/{memberId}/follow/{targetId}")
    public FollowResponseDTO follow(@PathVariable Long memberId, @PathVariable Long targetId) {
        return followService.follow(memberId, targetId);
    }

    // 언팔로우
    @DeleteMapping("/{memberId}/unfollow/{targetId}")
    public void unfollow(@PathVariable Long memberId, @PathVariable Long targetId) {
        followService.unfollow(memberId, targetId);
    }
}
