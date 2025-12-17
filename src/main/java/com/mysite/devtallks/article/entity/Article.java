package com.mysite.devtallks.article.entity;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.comment.entity.ArticleComment;
import com.mysite.devtallks.article.entity.ArticleLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private int viewCount;

    @Lob
    private String content;

    private String title;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleLike> likes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleComment> comments;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
