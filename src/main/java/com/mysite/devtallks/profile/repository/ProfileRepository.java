package com.mysite.devtallks.profile.repository;

import com.mysite.devtallks.profile.entity.Profile;
import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByMember(Member member);
    Optional<Profile> findTopByMemberOrderByCreatedAtDesc(Member member);
}
