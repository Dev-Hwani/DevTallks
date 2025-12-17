package com.mysite.devtallks.feed.service;

import com.mysite.devtallks.comment.repository.FeedCommentRepository;
import com.mysite.devtallks.feed.dto.FeedRequestDTO;
import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.entity.FeedLike;
import com.mysite.devtallks.feed.event.FeedEventPublisher;
import com.mysite.devtallks.feed.repository.FeedLikeRepository;
import com.mysite.devtallks.feed.repository.FeedRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final MemberRepository memberRepository;
    private final FeedEventPublisher feedEventPublisher;

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
        FeedResponseDTO dto = mapToResponseDTO(savedFeed);
        feedEventPublisher.publishFeedCreated(dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public FeedResponseDTO getFeedDTO(Long feedId) {
        Feed feed = getFeed(feedId);
        return mapToResponseDTO(feed);
    }

    @Transactional
    public FeedResponseDTO updateFeedDTO(Long feedId, Long requesterId, FeedRequestDTO requestDTO) {
        Feed feed = getFeed(feedId);
        ensureOwner(feed.getMember().getId(), requesterId, "자신의 피드만 수정할 수 있습니다.");

        if (requestDTO.getContent() != null) feed.setContent(requestDTO.getContent());
        if (requestDTO.getImageUrl() != null) feed.setImageUrl(requestDTO.getImageUrl());

        feed.setUpdatedAt(LocalDateTime.now());
        Feed updatedFeed = feedRepository.save(feed);
        FeedResponseDTO dto = mapToResponseDTO(updatedFeed);
        feedEventPublisher.publishFeedUpdated(dto);
        return dto;
    }

    @Transactional
    public void deleteFeed(Long feedId, Long requesterId) {
        Feed feed = getFeed(feedId);
        ensureOwner(feed.getMember().getId(), requesterId, "자신의 피드만 삭제할 수 있습니다.");
        feedRepository.delete(feed);
        feedEventPublisher.publishFeedDeleted(feedId);
    }

    @Transactional(readOnly = true)
    public Page<FeedResponseDTO> getFeedsByMemberDTO(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return feedRepository.findByMember(member, pageable)
                .map(this::mapToResponseDTO);
    }

    @Transactional
    public FeedLike likeFeed(Long memberId, Long feedId) {
        Feed feed = getFeed(feedId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        FeedLike like = feedLikeRepository.findByFeedAndMember(feed, member)
                .orElseGet(() -> feedLikeRepository.save(
                        FeedLike.builder()
                                .feed(feed)
                                .member(member)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
        long likeCount = feedLikeRepository.countByFeed(feed);
        feedEventPublisher.publishLikeUpdated(feedId, likeCount, true);
        return like;
    }

    @Transactional
    public void unlikeFeed(Long memberId, Long feedId) {
        Feed feed = getFeed(feedId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        feedLikeRepository.findByFeedAndMember(feed, member)
                .ifPresent(feedLikeRepository::delete);
        long likeCount = feedLikeRepository.countByFeed(feed);
        feedEventPublisher.publishLikeUpdated(feedId, likeCount, false);
    }

    private Feed getFeed(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다."));
    }

    private FeedResponseDTO mapToResponseDTO(Feed feed) {
        return FeedResponseDTO.builder()
                .id(feed.getId())
                .memberId(feed.getMember().getId())
                .memberNickname(feed.getMember().getNickname())
                .content(feed.getContent())
                .imageUrl(feed.getImageUrl())
                .likeCount(feedLikeRepository.countByFeed(feed))
                .commentCount(feedCommentRepository.countByFeed(feed))
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }

    private void ensureOwner(Long ownerId, Long requesterId, String message) {
        if (!ownerId.equals(requesterId)) {
            throw new AccessDeniedException(message);
        }
    }
}
