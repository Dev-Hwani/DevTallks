package com.mysite.devtallks.comment.repository;

import com.mysite.devtallks.comment.entity.ArticleComment;
import com.mysite.devtallks.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    Page<ArticleComment> findByArticle(Article article, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Page<ArticleComment> findAllByArticleOrderByCreatedAtAsc(Article article, Pageable pageable);

    long countByArticle(Article article);
}
