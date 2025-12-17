package com.mysite.devtallks.feed.controller;

import com.mysite.devtallks.comment.dto.FeedCommentRequestDTO;
import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import com.mysite.devtallks.comment.service.FeedCommentService;
import com.mysite.devtallks.common.dto.ResponseDto;
import com.mysite.devtallks.feed.dto.FeedRequestDTO;
import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import com.mysite.devtallks.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.devtallks.common.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final FeedCommentService feedCommentService;

    @PostMapping
    public ResponseDto<FeedResponseDTO> createFeed(@AuthenticationPrincipal CustomUserDetails user,
                                                   @Valid @RequestBody FeedRequestDTO requestDTO) {
        return ResponseDto.ok(feedService.createFeedDTO(user.getMemberId(), requestDTO));
    }

    @GetMapping("/{feedId}")
    public ResponseDto<FeedResponseDTO> getFeed(@PathVariable Long feedId) {
        return ResponseDto.ok(feedService.getFeedDTO(feedId));
    }

    @PutMapping("/{feedId}")
    public ResponseDto<FeedResponseDTO> updateFeed(@PathVariable Long feedId,
                                                   @AuthenticationPrincipal CustomUserDetails user,
                                                   @Valid @RequestBody FeedRequestDTO requestDTO) {
        return ResponseDto.ok(feedService.updateFeedDTO(feedId, user.getMemberId(), requestDTO));
    }

    @DeleteMapping("/{feedId}")
    public ResponseDto<Void> deleteFeed(@PathVariable Long feedId,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        feedService.deleteFeed(feedId, user.getMemberId());
        return ResponseDto.ok(null, "Feed deleted");
    }

    @GetMapping("/member/{memberId}")
    public ResponseDto<Page<FeedResponseDTO>> getFeedsByMember(@PathVariable Long memberId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(feedService.getFeedsByMemberDTO(memberId, pageable));
    }

    @GetMapping("/{feedId}/comments")
    public ResponseDto<Page<FeedCommentResponseDTO>> getComments(@PathVariable Long feedId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(feedCommentService.getCommentsByFeedDTO(feedId, pageable));
    }

    @PostMapping("/{feedId}/comments")
    public ResponseDto<FeedCommentResponseDTO> addComment(@PathVariable Long feedId,
                                                          @AuthenticationPrincipal CustomUserDetails user,
                                                          @Valid @RequestBody FeedCommentRequestDTO requestDTO) {
        return ResponseDto.ok(feedCommentService.createCommentDTO(feedId, user.getMemberId(), requestDTO));
    }

    @PutMapping("/{feedId}/comments/{commentId}")
    public ResponseDto<FeedCommentResponseDTO> updateComment(@PathVariable Long feedId,
                                                             @PathVariable Long commentId,
                                                             @AuthenticationPrincipal CustomUserDetails user,
                                                             @Valid @RequestBody FeedCommentRequestDTO requestDTO) {
        return ResponseDto.ok(feedCommentService.updateCommentDTO(commentId, user.getMemberId(), requestDTO));
    }

    @DeleteMapping("/{feedId}/comments/{commentId}")
    public ResponseDto<Void> deleteComment(@PathVariable Long feedId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        feedCommentService.deleteComment(commentId, user.getMemberId());
        return ResponseDto.ok(null, "Comment deleted");
    }

    @PostMapping("/{feedId}/like")
    public ResponseDto<Void> likeFeed(@PathVariable Long feedId,
                                      @AuthenticationPrincipal CustomUserDetails user) {
        feedService.likeFeed(user.getMemberId(), feedId);
        return ResponseDto.ok(null, "Feed liked");
    }

    @DeleteMapping("/{feedId}/like")
    public ResponseDto<Void> unlikeFeed(@PathVariable Long feedId,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        feedService.unlikeFeed(user.getMemberId(), feedId);
        return ResponseDto.ok(null, "Feed like removed");
    }
}
