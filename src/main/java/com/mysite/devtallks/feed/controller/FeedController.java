package com.mysite.devtallks.feed.controller;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.comment.entity.FeedComment;
import com.mysite.devtallks.feed.service.FeedService;
import com.mysite.devtallks.comment.service.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final FeedCommentService feedCommentService;

    // -------------------- Feed CRUD --------------------

    @PostMapping
    public Feed createFeed(@RequestBody Feed feed) {
        return feedService.createFeed(feed);
    }

    @GetMapping("/{feedId}")
    public Feed getFeed(@PathVariable Long feedId) {
        return feedService.getFeed(feedId);
    }

    @PutMapping("/{feedId}")
    public Feed updateFeed(@PathVariable Long feedId, @RequestBody Feed feed) {
        return feedService.updateFeed(feedId, feed);
    }

    @DeleteMapping("/{feedId}")
    public void deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
    }

    @GetMapping("/member/{memberId}")
    public Page<Feed> getFeedsByMember(@PathVariable Long memberId, Pageable pageable) {
        return feedService.getFeedsByMember(memberId, pageable);
    }

    // -------------------- Feed Comment CRUD --------------------

    // 댓글 조회 (Pageable)
    @GetMapping("/{feedId}/comments")
    public Page<FeedComment> getComments(@PathVariable Long feedId, Pageable pageable) {
        return feedCommentService.getCommentsByFeed(feedId, pageable);
    }

    // 댓글 작성
    @PostMapping("/{feedId}/comments")
    public FeedComment addComment(@PathVariable Long feedId, @RequestBody FeedComment comment) {
        return feedCommentService.addComment(feedId, comment);
    }

    // 댓글 수정
    @PutMapping("/{feedId}/comments/{commentId}")
    public FeedComment updateComment(@PathVariable Long feedId,
                                     @PathVariable Long commentId,
                                     @RequestBody FeedComment updatedComment) {
        return feedCommentService.updateComment(commentId, updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{feedId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long feedId,
                              @PathVariable Long commentId) {
        feedCommentService.deleteComment(commentId);
    }

    // -------------------- Feed Like --------------------

    @PostMapping("/{feedId}/like/{memberId}")
    public void likeFeed(@PathVariable Long feedId, @PathVariable Long memberId) {
        feedService.likeFeed(feedId, memberId);
    }

    @DeleteMapping("/{feedId}/like/{memberId}")
    public void unlikeFeed(@PathVariable Long feedId, @PathVariable Long memberId) {
        feedService.unlikeFeed(feedId, memberId);
    }
}
