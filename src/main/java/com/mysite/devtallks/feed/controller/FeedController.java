package com.mysite.devtallks.feed.controller;

import com.mysite.devtallks.feed.dto.FeedRequestDTO;
import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import com.mysite.devtallks.comment.dto.FeedCommentRequestDTO;
import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import com.mysite.devtallks.feed.service.FeedService;
import com.mysite.devtallks.comment.service.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final FeedCommentService feedCommentService;

    // ===========================
    // Feed CRUD (DTO 연동)
    // ===========================

    // Feed 작성
    @PostMapping
    public FeedResponseDTO createFeed(@RequestParam Long memberId,
                                      @RequestBody FeedRequestDTO requestDTO) {
        return feedService.createFeedDTO(memberId, requestDTO);
    }

    // Feed 단일 조회
    @GetMapping("/{feedId}")
    public FeedResponseDTO getFeed(@PathVariable Long feedId) {
        return feedService.getFeedDTO(feedId);
    }

    // Feed 수정
    @PutMapping("/{feedId}")
    public FeedResponseDTO updateFeed(@PathVariable Long feedId,
                                      @RequestBody FeedRequestDTO requestDTO) {
        return feedService.updateFeedDTO(feedId, requestDTO);
    }

    // Feed 삭제
    @DeleteMapping("/{feedId}")
    public void deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
    }

    // 회원별 Feed 조회 (페이징)
    @GetMapping("/member/{memberId}")
    public Page<FeedResponseDTO> getFeedsByMember(@PathVariable Long memberId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedService.getFeedsByMemberDTO(memberId, pageable);
    }

    // ===========================
    // Feed Comment CRUD (DTO 연동)
    // ===========================

    // 댓글 조회
    @GetMapping("/{feedId}/comments")
    public Page<FeedCommentResponseDTO> getComments(@PathVariable Long feedId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedCommentService.getCommentsByFeedDTO(feedId, pageable);
    }

    // 댓글 작성
    @PostMapping("/{feedId}/comments")
    public FeedCommentResponseDTO addComment(@PathVariable Long feedId,
                                             @RequestParam Long memberId,
                                             @RequestBody FeedCommentRequestDTO requestDTO) {
        return feedCommentService.createCommentDTO(feedId, memberId, requestDTO);
    }

    // 댓글 수정
    @PutMapping("/{feedId}/comments/{commentId}")
    public FeedCommentResponseDTO updateComment(@PathVariable Long feedId,
                                                @PathVariable Long commentId,
                                                @RequestBody FeedCommentRequestDTO requestDTO) {
        return feedCommentService.updateCommentDTO(commentId, requestDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{feedId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long feedId,
                              @PathVariable Long commentId) {
        feedCommentService.deleteComment(commentId);
    }

    // ===========================
    // Feed Like (기존과 동일)
    // ===========================

    @PostMapping("/{feedId}/like/{memberId}")
    public void likeFeed(@PathVariable Long feedId, @PathVariable Long memberId) {
        feedService.likeFeed(memberId, feedId);
    }

    @DeleteMapping("/{feedId}/like/{memberId}")
    public void unlikeFeed(@PathVariable Long feedId, @PathVariable Long memberId) {
        feedService.unlikeFeed(memberId, feedId);
    }
}
