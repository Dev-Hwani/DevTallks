package com.mysite.devtallks.member.controller;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.service.MemberService;
import com.mysite.devtallks.follow.entity.Follow;
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
    public Member getMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

    // 팔로우 리스트 조회 (페이징)
    @GetMapping("/{memberId}/followers")
    public Page<Member> getFollowers(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return followService.getFollowers(memberId, pageable);
    }


    // 팔로잉 리스트 조회 (페이징)
    @GetMapping("/{memberId}/followings")
    public Page<Member> getFollowings(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return followService.getFollowings(memberId, pageable);
    }


    // 팔로우 추가
    @PostMapping("/{memberId}/follow/{targetId}")
    public Follow follow(@PathVariable Long memberId, @PathVariable Long targetId) {
        return followService.follow(memberId, targetId);
    }

    // 언팔로우
    @DeleteMapping("/{memberId}/unfollow/{targetId}")
    public void unfollow(@PathVariable Long memberId, @PathVariable Long targetId) {
        followService.unfollow(memberId, targetId);
    }
}
