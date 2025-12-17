package com.mysite.devtallks.feed.service;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.entity.FeedLike;
import com.mysite.devtallks.feed.repository.FeedLikeRepository;
import com.mysite.devtallks.feed.repository.FeedRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FeedLike likeFeed(Long feedId, Long memberId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return feedLikeRepository.findByFeedAndMember(feed, member)
                .orElseGet(() -> feedLikeRepository.save(
                        FeedLike.builder()
                                .feed(feed)
                                .member(member)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    @Transactional
    public void unlikeFeed(Long feedId, Long memberId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        feedLikeRepository.findByFeedAndMember(feed, member)
                .ifPresent(feedLikeRepository::delete);
    }
}
