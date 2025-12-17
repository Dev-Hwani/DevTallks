package com.mysite.devtallks.comment.service;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.repository.ArticleRepository;
import com.mysite.devtallks.comment.dto.ArticleCommentResponseDTO;
import com.mysite.devtallks.comment.entity.ArticleComment;
import com.mysite.devtallks.comment.repository.ArticleCommentRepository;
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
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleCommentResponseDTO createCommentDTO(Long articleId, Long memberId, String content, Long parentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
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
        ArticleComment saved = articleCommentRepository.save(comment);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<ArticleCommentResponseDTO> getCommentsByArticleDTO(Long articleId, Pageable pageable) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Page<ArticleComment> comments = articleCommentRepository.findByArticle(article, pageable);
        return comments.map(this::toDTO);
    }

    @Transactional
    public ArticleCommentResponseDTO updateCommentDTO(Long commentId, Long requesterId, String content) {
        ArticleComment comment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 댓글만 수정할 수 있습니다.");
        }
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        ArticleComment updated = articleCommentRepository.save(comment);
        return toDTO(updated);
    }

    @Transactional
    public void deleteComment(Long commentId, Long requesterId) {
        ArticleComment comment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 댓글만 삭제할 수 있습니다.");
        }
        articleCommentRepository.delete(comment);
    }

    private ArticleCommentResponseDTO toDTO(ArticleComment comment) {
        return ArticleCommentResponseDTO.builder()
                .id(comment.getId())
                .articleId(comment.getArticle().getId())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
