package com.mysite.devtallks.article.controller;

import com.mysite.devtallks.article.dto.ArticleRequestDTO;
import com.mysite.devtallks.article.dto.ArticleResponseDTO;
import com.mysite.devtallks.article.service.ArticleService;
import com.mysite.devtallks.comment.dto.ArticleCommentRequestDTO;
import com.mysite.devtallks.comment.dto.ArticleCommentResponseDTO;
import com.mysite.devtallks.comment.service.ArticleCommentService;
import com.mysite.devtallks.common.dto.ResponseDto;
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
import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;

    @GetMapping
    public ResponseDto<Page<ArticleResponseDTO>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(articleService.getArticlesDTO(pageable));
    }

    @GetMapping("/popular")
    public ResponseDto<List<ArticleResponseDTO>> getPopularArticles(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseDto.ok(articleService.getPopularArticles(limit));
    }

    @GetMapping("/member/{memberId}")
    public ResponseDto<Page<ArticleResponseDTO>> getArticlesByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(articleService.getArticlesByMemberDTO(memberId, pageable));
    }

    @PostMapping
    public ResponseDto<ArticleResponseDTO> createArticle(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ArticleRequestDTO requestDTO) {
        return ResponseDto.ok(articleService.createArticle(user.getMemberId(), requestDTO));
    }

    @GetMapping("/{articleId}")
    public ResponseDto<ArticleResponseDTO> getArticle(@PathVariable Long articleId) {
        return ResponseDto.ok(articleService.getArticleDTO(articleId));
    }

    @PutMapping("/{articleId}")
    public ResponseDto<ArticleResponseDTO> updateArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ArticleRequestDTO requestDTO) {
        return ResponseDto.ok(articleService.updateArticleDTO(articleId, user.getMemberId(), requestDTO));
    }

    @DeleteMapping("/{articleId}")
    public ResponseDto<Void> deleteArticle(@PathVariable Long articleId,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        articleService.deleteArticle(articleId, user.getMemberId());
        return ResponseDto.ok(null, "Article deleted");
    }

    @GetMapping("/{articleId}/comments")
    public ResponseDto<Page<ArticleCommentResponseDTO>> getComments(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseDto.ok(articleCommentService.getCommentsByArticleDTO(articleId, pageable));
    }

    @PostMapping("/{articleId}/comments")
    public ResponseDto<ArticleCommentResponseDTO> addComment(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ArticleCommentRequestDTO request) {
        return ResponseDto.ok(articleCommentService.createCommentDTO(articleId, user.getMemberId(), request.getContent(), request.getParentId()));
    }

    @PutMapping("/{articleId}/comments/{commentId}")
    public ResponseDto<ArticleCommentResponseDTO> updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ArticleCommentRequestDTO request) {
        return ResponseDto.ok(articleCommentService.updateCommentDTO(commentId, user.getMemberId(), request.getContent()));
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseDto<Void> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user) {
        articleCommentService.deleteComment(commentId, user.getMemberId());
        return ResponseDto.ok(null, "Comment deleted");
    }
}
