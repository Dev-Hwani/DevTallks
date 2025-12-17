package com.mysite.devtallks.feed.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedLikeEvent {
    private Long feedId;
    private long likeCount;
    private String type; // LIKED or UNLIKED
}
