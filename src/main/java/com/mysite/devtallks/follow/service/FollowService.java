package com.mysite.devtallks.follow.service;

import com.mysite.devtallks.follow.entity.Follow;
import com.mysite.devtallks.follow.repository.FollowRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Follow follow(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워 회원이 존재하지 않습니다."));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 회원이 존재하지 않습니다."));

        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseGet(() -> followRepository.save(
                        Follow.builder()
                                .follower(follower)
                                .following(following)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워 회원이 존재하지 않습니다."));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 회원이 존재하지 않습니다."));

        followRepository.findByFollowerAndFollowing(follower, following)
                .ifPresent(followRepository::delete);
    }

    @Transactional(readOnly = true)
    public Page<Member> getFollowers(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Page<Follow> followPage = followRepository.findByFollowing(member, pageable);

        return followPage.map(Follow::getFollower);
    }

    @Transactional(readOnly = true)
    public Page<Member> getFollowings(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Page<Follow> followPage = followRepository.findByFollower(member, pageable);

        return followPage.map(Follow::getFollowing);
    }

}
