package com.mysite.devtallks.feed.repository;

import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findByMember(Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"comments", "likes"})
    Page<Feed> findAll(Pageable pageable);
}
