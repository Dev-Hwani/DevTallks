package com.mysite.devtallks.article.service;

import com.mysite.devtallks.article.dto.ArticleRequestDTO;
import com.mysite.devtallks.article.dto.ArticleResponseDTO;
import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.event.ArticleEventPublisher;
import com.mysite.devtallks.article.repository.ArticleLikeRepository;
import com.mysite.devtallks.article.repository.ArticleRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleViewCacheService articleViewCacheService;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleEventPublisher articleEventPublisher;

    @Transactional
    public ArticleResponseDTO createArticle(Long memberId, ArticleRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        Article article = Article.builder()
                .member(member)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Article savedArticle = articleRepository.save(article);
        long viewCount = articleViewCacheService.getTotalViewCount(savedArticle.getId(), savedArticle.getViewCount());
        ArticleResponseDTO response = mapToResponseDTO(savedArticle, viewCount);
        return response;
    }

    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleDTO(Long articleId) {
        Article article = getArticle(articleId);
        long views = articleViewCacheService.incrementAndGet(article.getId(), article.getViewCount());
        ArticleResponseDTO dto = mapToResponseDTO(article, views);
        articleEventPublisher.publishViewUpdated(articleId, views);
        return dto;
    }

    @Transactional
    public ArticleResponseDTO updateArticleDTO(Long articleId, Long requesterId, ArticleRequestDTO requestDTO) {
        Article existingArticle = getArticle(articleId);
        if (!existingArticle.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 게시글만 수정할 수 있습니다.");
        }
        existingArticle.setTitle(requestDTO.getTitle());
        existingArticle.setContent(requestDTO.getContent());
        existingArticle.setUpdatedAt(LocalDateTime.now());

        Article updatedArticle = articleRepository.save(existingArticle);
        long views = articleViewCacheService.getTotalViewCount(updatedArticle.getId(), updatedArticle.getViewCount());
        ArticleResponseDTO dto = mapToResponseDTO(updatedArticle, views);
        return dto;
    }

    @Transactional
    public void deleteArticle(Long articleId, Long requesterId) {
        Article article = getArticle(articleId);
        if (!article.getMember().getId().equals(requesterId)) {
            throw new AccessDeniedException("자신의 게시글만 삭제할 수 있습니다.");
        }
        articleViewCacheService.evict(articleId);
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getArticlesByMemberDTO(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        return articleRepository.findByMember(member, pageable)
                .map(article -> mapToResponseDTO(article, articleViewCacheService.getTotalViewCount(article.getId(), article.getViewCount())));
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getArticlesDTO(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(article -> mapToResponseDTO(article, articleViewCacheService.getTotalViewCount(article.getId(), article.getViewCount())));
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getPopularArticles(int limit) {
        List<Long> ids = articleViewCacheService.getPopularArticleIds(limit);
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        List<Article> articles = articleRepository.findAllById(ids);
        Map<Long, Article> articleMap = new HashMap<>();
        for (Article article : articles) {
            articleMap.put(article.getId(), article);
        }

        List<ArticleResponseDTO> result = new ArrayList<>();
        for (Long id : ids) {
            Article article = articleMap.get(id);
            if (article != null) {
                long views = articleViewCacheService.getTotalViewCount(article.getId(), article.getViewCount());
                result.add(mapToResponseDTO(article, views));
            }
        }
        return result;
    }

    private Article getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found."));
    }

    private ArticleResponseDTO mapToResponseDTO(Article article, long viewCount) {
        long likeCount = articleLikeRepository.countByArticle(article);
        long commentCount = articleCommentRepository.countByArticle(article);

        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .memberId(article.getMember().getId())
                .memberNickname(article.getMember().getNickname())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .viewCount(viewCount)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

}
