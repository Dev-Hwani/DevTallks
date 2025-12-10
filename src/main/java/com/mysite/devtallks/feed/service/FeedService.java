package com.mysite.devtallks.feed.service;

import com.mysite.devtallks.feed.dto.FeedRequestDTO;
import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.entity.FeedLike;
import com.mysite.devtallks.feed.repository.FeedLikeRepository;
import com.mysite.devtallks.feed.repository.FeedRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        if (updatedFeed.getContent() != null) feed.setContent(updatedFeed.getContent());
        if (updatedFeed.getImageUrl() != null) feed.setImageUrl(updatedFeed.getImageUrl());

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


    // ===============================
    // DTO 연동 메서드 추가
    // ===============================

    @Transactional
    public FeedResponseDTO createFeedDTO(Long memberId, FeedRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Feed feed = Feed.builder()
                .member(member)
                .content(requestDTO.getContent())
                .imageUrl(requestDTO.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Feed savedFeed = feedRepository.save(feed);
        return mapToResponseDTO(savedFeed);
    }

    @Transactional(readOnly = true)
    public FeedResponseDTO getFeedDTO(Long feedId) {
        Feed feed = getFeed(feedId);
        return mapToResponseDTO(feed);
    }

    @Transactional
    public FeedResponseDTO updateFeedDTO(Long feedId, FeedRequestDTO requestDTO) {
        Feed feed = getFeed(feedId);

        if (requestDTO.getContent() != null) feed.setContent(requestDTO.getContent());
        if (requestDTO.getImageUrl() != null) feed.setImageUrl(requestDTO.getImageUrl());

        feed.setUpdatedAt(LocalDateTime.now());
        Feed updatedFeed = feedRepository.save(feed);
        return mapToResponseDTO(updatedFeed);
    }

    @Transactional(readOnly = true)
    public Page<FeedResponseDTO> getFeedsByMemberDTO(Long memberId, Pageable pageable) {
        return getFeedsByMember(memberId, pageable)
                .map(this::mapToResponseDTO);
    }

    // 엔티티 → DTO 변환 헬퍼
    private FeedResponseDTO mapToResponseDTO(Feed feed) {
        return FeedResponseDTO.builder()
                .id(feed.getId())
                .memberId(feed.getMember().getId())
                .memberNickname(feed.getMember().getNickname())
                .content(feed.getContent())
                .imageUrl(feed.getImageUrl())
                .likeCount(feed.getLikes() != null ? feed.getLikes().size() : 0)
                .commentCount(feed.getComments() != null ? feed.getComments().size() : 0)
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}
