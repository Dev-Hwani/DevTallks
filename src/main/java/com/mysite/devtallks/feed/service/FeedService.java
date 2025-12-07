package com.mysite.devtallks.feed.service;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.entity.FeedLike;
import com.mysite.devtallks.feed.repository.FeedRepository;
import com.mysite.devtallks.feed.repository.FeedLikeRepository;
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
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Feed createFeed(Feed feed) {
        feed.setCreatedAt(LocalDateTime.now());
        feed.setUpdatedAt(LocalDateTime.now());
        return feedRepository.save(feed);
    }

    @Transactional(readOnly = true)
    public Feed getFeed(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
    }

    @Transactional
    public Feed updateFeed(Long feedId, Feed updatedFeed) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        
        if (updatedFeed.getContent() != null) {
            feed.setContent(updatedFeed.getContent());
        }
        if (updatedFeed.getImageUrl() != null) {
            feed.setImageUrl(updatedFeed.getImageUrl());
        }
        
        feed.setUpdatedAt(LocalDateTime.now());
        return feedRepository.save(feed);
    }


    @Transactional
    public void deleteFeed(Long feedId) {
        feedRepository.deleteById(feedId);
    }

    @Transactional(readOnly = true)
    public Page<Feed> getFeedsByMember(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return feedRepository.findByMember(member, pageable);
    }
    
    @Transactional
    public FeedLike likeFeed(Long memberId, Long feedId) {
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
    public void unlikeFeed(Long memberId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        feedLikeRepository.findByFeedAndMember(feed, member)
                .ifPresent(feedLikeRepository::delete);
    }

}
