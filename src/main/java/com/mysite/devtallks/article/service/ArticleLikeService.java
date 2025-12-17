package com.mysite.devtallks.article.service;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.article.entity.ArticleLike;
import com.mysite.devtallks.article.repository.ArticleLikeRepository;
import com.mysite.devtallks.article.repository.ArticleRepository;
import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleLike likeArticle(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return articleLikeRepository.findByArticleAndMember(article, member)
                .orElseGet(() -> articleLikeRepository.save(
                        ArticleLike.builder()
                                .article(article)
                                .member(member)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));
    }

    @Transactional
    public void unlikeArticle(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        articleLikeRepository.findByArticleAndMember(article, member)
                .ifPresent(articleLikeRepository::delete);
    }
}
