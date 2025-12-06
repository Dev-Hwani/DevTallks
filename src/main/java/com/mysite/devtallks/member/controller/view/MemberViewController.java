package com.mysite.devtallks.member.controller.view;

import com.mysite.devtallks.member.service.MemberService;
import com.mysite.devtallks.member.service.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * JSP/뷰 렌더링용 컨트롤러
 * 경로: /members/** (페이지 라우팅)
 *
 * 예: /members/{username} -> 포트폴리오/프로필 페이지
 */
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    /**
     * 프로필 페이지
     */
    @GetMapping("/{username}")
    public String profileView(@PathVariable("username") String username, Model model) {
        MemberResponse resp = memberService.findByUsername(username);
        model.addAttribute("member", resp);
        // view name: /WEB-INF/views/members/profile.jsp 또는 members/profile
        return "members/profile";
    }
}
