package com.mysite.devtallks.article.controller;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.service.ArticleService;
import com.mysite.devtallks.comment.entity.ArticleComment;
import com.mysite.devtallks.comment.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;

    // 게시글 목록 조회 (페이징)
    @GetMapping
    public Page<Article> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.getArticles(pageable);
    }

    // 특정 회원 게시글 조회 (페이징)
    @GetMapping("/member/{memberId}")
    public Page<Article> getArticlesByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.getArticlesByMember(memberId, pageable);
    }

    // 게시글 작성
    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    // 게시글 단일 조회
    @GetMapping("/{articleId}")
    public Article getArticle(@PathVariable Long articleId) {
        return articleService.getArticle(articleId);
    }

    // 게시글 수정
    @PutMapping("/{articleId}")
    public Article updateArticle(
            @PathVariable Long articleId,
            @RequestBody Article article) {
        return articleService.updateArticle(articleId, article);
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public void deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
    }

    // 댓글 목록 조회 (페이징)
    @GetMapping("/{articleId}/comments")
    public Page<ArticleComment> getComments(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleCommentService.getCommentsByArticle(articleId, pageable);
    }

    // 댓글 작성 (대댓글 지원)
    @PostMapping("/{articleId}/comments")
    public ArticleComment addComment(
            @PathVariable Long articleId,
            @RequestParam Long memberId,
            @RequestParam(required = false) Long parentId,
            @RequestBody String content) {
        return articleCommentService.createComment(articleId, memberId, content, parentId);
    }

    // 댓글 수정
    @PutMapping("/{articleId}/comments/{commentId}")
    public ArticleComment updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestBody String content) {
        return articleCommentService.updateComment(commentId, content);
    }

    // 댓글 삭제
    @DeleteMapping("/{articleId}/comments/{commentId}")
    public void deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId) {
        articleCommentService.deleteComment(commentId);
    }
}
