package com.mysite.devtallks.feed.entity;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.comment.entity.FeedComment;
import com.mysite.devtallks.feed.entity.FeedLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feed")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedLike> likes;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedComment> comments;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
