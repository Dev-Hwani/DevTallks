package com.mysite.devtallks.article.service;

import com.mysite.devtallks.article.dto.ArticleRequestDTO;
import com.mysite.devtallks.article.dto.ArticleResponseDTO;
import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.repository.ArticleRepository;
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
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Article createArticle(Article article) {
        Member member = memberRepository.findById(article.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Article newArticle = Article.builder()
                .member(member)
                .title(article.getTitle())
                .content(article.getContent())
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return articleRepository.save(newArticle);
    }

    // DTO 기반 게시글 생성
    @Transactional
    public ArticleResponseDTO createArticle(Long memberId, ArticleRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Article article = Article.builder()
                .member(member)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Article savedArticle = articleRepository.save(article);
        return mapToResponseDTO(savedArticle);
    }

    @Transactional(readOnly = true)
    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    // DTO 기반 게시글 단일 조회
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleDTO(Long articleId) {
        Article article = getArticle(articleId);
        return mapToResponseDTO(article);
    }

    @Transactional
    public Article updateArticle(Long articleId, Article article) {
        Article existingArticle = getArticle(articleId);
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        existingArticle.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(existingArticle);
    }

    // DTO 기반 게시글 수정
    @Transactional
    public ArticleResponseDTO updateArticleDTO(Long articleId, ArticleRequestDTO requestDTO) {
        Article existingArticle = getArticle(articleId);
        existingArticle.setTitle(requestDTO.getTitle());
        existingArticle.setContent(requestDTO.getContent());
        existingArticle.setUpdatedAt(LocalDateTime.now());

        Article updatedArticle = articleRepository.save(existingArticle);
        return mapToResponseDTO(updatedArticle);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByMember(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return articleRepository.findByMember(member, pageable);
    }

    // DTO 기반 회원별 게시글 조회
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getArticlesByMemberDTO(Long memberId, Pageable pageable) {
        return getArticlesByMember(memberId, pageable)
                .map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    // DTO 기반 전체 게시글 조회
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getArticlesDTO(Pageable pageable) {
        return getArticles(pageable)
                .map(this::mapToResponseDTO);
    }

    // Entity → DTO 변환 헬퍼
    private ArticleResponseDTO mapToResponseDTO(Article article) {
        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .memberId(article.getMember().getId())
                .memberNickname(article.getMember().getNickname())
                .likeCount(article.getLikes() != null ? article.getLikes().size() : 0)
                .commentCount(article.getComments() != null ? article.getComments().size() : 0)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

}
