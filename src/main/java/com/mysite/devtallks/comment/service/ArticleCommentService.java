package com.mysite.devtallks.comment.service;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.repository.ArticleRepository;
import com.mysite.devtallks.comment.entity.ArticleComment;
import com.mysite.devtallks.comment.repository.ArticleCommentRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleComment createComment(Long articleId, Long memberId, String content, Long parentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        ArticleComment comment = ArticleComment.builder()
                .article(article)
                .member(member)
                .content(content)
                .parentId(parentId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return articleCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        articleCommentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleComment> getCommentsByArticle(Long articleId, int page, int size) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Pageable pageable = PageRequest.of(page, size);
        return articleCommentRepository.findByArticle(article, pageable);
    }

    public Page<ArticleComment> getCommentsByArticle(Long articleId, Pageable pageable) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return articleCommentRepository.findByArticle(article, pageable);
    }

    public ArticleComment addComment(Long articleId, ArticleComment comment) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        comment.setArticle(article);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return articleCommentRepository.save(comment);
    }
    
    @Transactional
    public ArticleComment updateComment(Long commentId, String content) {
        ArticleComment comment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        return articleCommentRepository.save(comment);
    }

}
