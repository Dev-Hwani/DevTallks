package com.mysite.devtallks.member.service;

import com.mysite.devtallks.member.dto.MemberRequestDTO;
import com.mysite.devtallks.member.dto.MemberResponseDTO;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /** -----------------------
     *  Entity -> DTO 변환 메서드
     * ------------------------*/
    private MemberResponseDTO toDTO(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    /** -----------------------
     *  회원 생성 
     * ------------------------*/
    @Transactional
    public MemberResponseDTO createMember(MemberRequestDTO.SignUp request) {

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(request.getPassword())  // 암호화 필요하면 여기서 처리
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Member saved = memberRepository.save(member);
        return toDTO(saved);
    }

    /** -----------------------
     *  회원 조회
     * ------------------------*/
    @Transactional(readOnly = true)
    public MemberResponseDTO getMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return toDTO(member);
    }

    /** -----------------------
     *  전체 회원 목록 조회
     * ------------------------*/
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** -----------------------
     *  회원 정보 수정 
     * ------------------------*/
    @Transactional
    public MemberResponseDTO updateMember(Long memberId, MemberRequestDTO.UpdateInfo request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if (request.getName() != null) member.setName(request.getName());
        if (request.getNickname() != null) member.setNickname(request.getNickname());
        if (request.getPassword() != null) member.setPassword(request.getPassword());

        member.setUpdatedAt(LocalDateTime.now());

        Member saved = memberRepository.save(member);
        return toDTO(saved);
    }

    /** -----------------------
     *  회원 삭제
     * ------------------------*/
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
