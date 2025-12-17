package com.mysite.devtallks.comment.service;

import com.mysite.devtallks.comment.dto.FeedCommentRequestDTO;
import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import com.mysite.devtallks.comment.entity.FeedComment;
import com.mysite.devtallks.comment.repository.FeedCommentRepository;
import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.event.FeedEventPublisher;
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
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedEventPublisher feedEventPublisher;

    @Transactional
    public FeedCommentResponseDTO createCommentDTO(Long feedId, Long memberId, FeedCommentRequestDTO requestDTO) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다."));
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
        FeedCommentResponseDTO response = mapToResponseDTO(savedComment);
        feedEventPublisher.publishCommentCreated(feedId, response);
        return response;
    }

    @Transactional(readOnly = true)
    public Page<FeedCommentResponseDTO> getCommentsByFeedDTO(Long feedId, Pageable pageable) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다."));
        return feedCommentRepository.findByFeed(feed, pageable)
                .map(this::mapToResponseDTO);
    }

    @Transactional
    public FeedCommentResponseDTO updateCommentDTO(Long commentId, Long requesterId, FeedCommentRequestDTO requestDTO) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 댓글만 수정할 수 있습니다.");
        }
        comment.setContent(requestDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        FeedComment updatedComment = feedCommentRepository.save(comment);
        FeedCommentResponseDTO response = mapToResponseDTO(updatedComment);
        feedEventPublisher.publishCommentUpdated(comment.getFeed().getId(), response);
        return response;
    }

    @Transactional
    public void deleteComment(Long commentId, Long requesterId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 댓글만 삭제할 수 있습니다.");
        }
        feedCommentRepository.delete(comment);
        feedEventPublisher.publishCommentDeleted(comment.getFeed().getId(), commentId);
    }

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
