package com.mysite.devtallks.comment.service;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.feed.repository.FeedRepository;
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

    // 댓글 작성
    @Transactional
    public FeedComment addComment(Long feedId, FeedComment comment) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        comment.setFeed(feed);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return feedCommentRepository.save(comment);
    }

    // 댓글 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<FeedComment> getCommentsByFeed(Long feedId, Pageable pageable) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
        return feedCommentRepository.findByFeed(feed, pageable);
    }

    // 댓글 수정
    @Transactional
    public FeedComment updateComment(Long commentId, FeedComment updatedComment) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.setContent(updatedComment.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        return feedCommentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        feedCommentRepository.deleteById(commentId);
    }
}
