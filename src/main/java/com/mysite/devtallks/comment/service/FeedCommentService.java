package com.mysite.devtallks.comment.service;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.repository.FeedRepository;
import com.mysite.devtallks.comment.dto.FeedCommentRequestDTO;
import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import com.mysite.devtallks.comment.entity.FeedComment;
import com.mysite.devtallks.comment.repository.FeedCommentRepository;
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
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    // ===========================
    @Transactional
    public FeedComment addComment(Long feedId, FeedComment comment) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        comment.setFeed(feed);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return feedCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<FeedComment> getCommentsByFeed(Long feedId, Pageable pageable) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        return feedCommentRepository.findByFeed(feed, pageable);
    }

    @Transactional
    public FeedComment updateComment(Long commentId, FeedComment updatedComment) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.setContent(updatedComment.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        return feedCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        feedCommentRepository.deleteById(commentId);
    }

    // ===========================
    // DTO 기반 메서드 추가
    // ===========================

    @Transactional
    public FeedCommentResponseDTO createCommentDTO(Long feedId, Long memberId, FeedCommentRequestDTO requestDTO) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        FeedComment comment = FeedComment.builder()
                .feed(feed)
                .member(member)
                .content(requestDTO.getContent())
                .parentId(requestDTO.getParentId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        FeedComment savedComment = feedCommentRepository.save(comment);
        return mapToResponseDTO(savedComment);
    }

    @Transactional(readOnly = true)
    public Page<FeedCommentResponseDTO> getCommentsByFeedDTO(Long feedId, Pageable pageable) {
        return getCommentsByFeed(feedId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Transactional
    public FeedCommentResponseDTO updateCommentDTO(Long commentId, FeedCommentRequestDTO requestDTO) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.setContent(requestDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        FeedComment updatedComment = feedCommentRepository.save(comment);
        return mapToResponseDTO(updatedComment);
    }

    // ===========================
    // Entity → DTO 변환 헬퍼
    // ===========================
    private FeedCommentResponseDTO mapToResponseDTO(FeedComment comment) {
        return FeedCommentResponseDTO.builder()
                .id(comment.getId())
                .feedId(comment.getFeed().getId())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
