package com.mysite.devtallks.comment.repository;

import com.mysite.devtallks.comment.entity.FeedComment;
import com.mysite.devtallks.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    Page<FeedComment> findByFeed(Feed feed, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Page<FeedComment> findAllByFeedOrderByCreatedAtAsc(Feed feed, Pageable pageable);

    long countByFeed(Feed feed);
}
