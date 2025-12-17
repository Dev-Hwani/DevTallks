package com.mysite.devtallks.member.controller;

import com.mysite.devtallks.common.dto.ResponseDto;
import com.mysite.devtallks.follow.dto.FollowResponseDTO;
import com.mysite.devtallks.follow.service.FollowService;
import com.mysite.devtallks.member.dto.MemberResponseDTO;
import com.mysite.devtallks.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;

    @GetMapping("/{memberId}")
    public ResponseDto<MemberResponseDTO> getMember(@PathVariable Long memberId) {
        return ResponseDto.ok(memberService.getMember(memberId));
    }

    @GetMapping("/{memberId}/followers")
    public ResponseDto<Page<FollowResponseDTO>> getFollowers(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(followService.getFollowersDTO(memberId, pageable));
    }

    @GetMapping("/{memberId}/followings")
    public ResponseDto<Page<FollowResponseDTO>> getFollowings(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(followService.getFollowingsDTO(memberId, pageable));
    }

    @PostMapping("/{targetId}/follow")
    public ResponseDto<FollowResponseDTO> follow(@AuthenticationPrincipal com.mysite.devtallks.common.security.CustomUserDetails user,
                                                 @PathVariable Long targetId) {
        return ResponseDto.ok(followService.follow(user.getMemberId(), targetId));
    }

    @DeleteMapping("/{targetId}/unfollow")
    public ResponseDto<Void> unfollow(@AuthenticationPrincipal com.mysite.devtallks.common.security.CustomUserDetails user,
                                      @PathVariable Long targetId) {
        followService.unfollow(user.getMemberId(), targetId);
        return ResponseDto.ok(null, "Unfollowed");
    }
}
