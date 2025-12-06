package com.mysite.devtallks.member.controller.closed;

import com.mysite.devtallks.common.dto.ApiResponse;
import com.mysite.devtallks.common.utils.SecurityUtils;
import com.mysite.devtallks.member.service.MemberService;
import com.mysite.devtallks.member.service.dto.request.MemberUpdateRequest;
import com.mysite.devtallks.member.service.dto.response.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 인증된 사용자만 접근 가능한 API
 * 경로: /api/members/**
 */
@RestController
@RequestMapping(path = "/api/members", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberClosedController {

    private final MemberService memberService;

    /**
     * 내 정보 조회
     * GET /api/members/me
     */
    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyProfile(Authentication authentication) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        MemberResponse resp = memberService.findByUsername(currentUsername);
        return ApiResponse.success(resp, "내 정보 조회 성공");
    }

    /**
     * 특정 회원 조회 (관리자 또는 본인)
     * GET /api/members/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getMember(@PathVariable("id") Long id, Authentication authentication) {
        authorizeIfNotAdminOrOwner(id, authentication);
        MemberResponse resp = memberService.findById(id);
        return ApiResponse.success(resp);
    }

    /**
     * 회원 정보 수정 (본인 또는 ADMIN)
     * PUT /api/members/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(@PathVariable("id") Long id,
                                                    @Valid @RequestBody MemberUpdateRequest req,
                                                    Authentication authentication) {
        authorizeIfNotAdminOrOwner(id, authentication);
        MemberResponse updated = memberService.updateMember(id, req);
        return ApiResponse.success(updated, "회원정보 수정 성공");
    }

    /**
     * 회원 삭제 (본인 또는 ADMIN)
     * DELETE /api/members/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable("id") Long id, Authentication authentication) {
        authorizeIfNotAdminOrOwner(id, authentication);
        memberService.deleteMember(id);
        return ApiResponse.success(null, "회원 삭제 성공");
    }

    /**
     * 권한 체크 헬퍼:
     * - ADMIN 권한이면 통과
     * - 아니면 현재 인증된 사용자의 id와 파라미터 id가 일치해야 함
     *
     * 구현 방식: 현재 SecurityUtils.getCurrentUsername()로 이메일(username)을 가져오고,
     * memberService.findByUsername(...)로 회원 정보를 가져와 id 비교
     */
    private void authorizeIfNotAdminOrOwner(Long targetMemberId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("인증 필요");
        }

        // 관리자 권한이 있으면 허용
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        if (isAdmin) return;

        // 본인인지 확인
        String currentUsername = SecurityUtils.getCurrentUsername();
        MemberResponse current = memberService.findByUsername(currentUsername);
        if (current == null || !current.getId().equals(targetMemberId)) {
            throw new org.springframework.security.access.AccessDeniedException("권한이 없습니다.");
        }
    }
}
