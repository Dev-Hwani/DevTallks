package com.mysite.devtallks.follow.repository;

import com.mysite.devtallks.follow.entity.Follow;
import com.mysite.devtallks.member.entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    Page<Follow> findByFollowing(Member following, Pageable pageable); // 팔로워 조회
    Page<Follow> findByFollower(Member follower, Pageable pageable);   // 팔로잉 조회
    
    List<Follow> findByFollower(Member follower);
    List<Follow> findByFollowing(Member following);
}
