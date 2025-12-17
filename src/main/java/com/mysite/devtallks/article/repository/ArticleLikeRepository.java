package com.mysite.devtallks.article.repository;

import com.mysite.devtallks.article.entity.ArticleLike;
import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findByArticleAndMember(Article article, Member member);

    long countByArticle(Article article);
}
