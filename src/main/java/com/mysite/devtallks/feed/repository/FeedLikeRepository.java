package com.mysite.devtallks.feed.repository;

import com.mysite.devtallks.feed.entity.FeedLike;
import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    Optional<FeedLike> findByFeedAndMember(Feed feed, Member member);

    long countByFeed(Feed feed);
}
