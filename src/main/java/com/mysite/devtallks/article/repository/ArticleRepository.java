package com.mysite.devtallks.article.repository;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByMember(Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"comments", "likes"})
    Optional<Article> findById(Long id);
}
