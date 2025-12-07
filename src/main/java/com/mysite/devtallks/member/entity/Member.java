package com.mysite.devtallks.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.devtallks.article.entity.Article;
import com.mysite.devtallks.feed.entity.Feed;
import com.mysite.devtallks.follow.entity.Follow;
import com.mysite.devtallks.profile.entity.Profile;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique=true)
    private String nickname;

    @Column(nullable=false)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Feed> feeds;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> followings;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Profile> profiles;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
