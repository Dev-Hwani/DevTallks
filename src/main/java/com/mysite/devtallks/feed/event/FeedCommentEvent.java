package com.mysite.devtallks.feed.event;

import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedCommentEvent {
    private String type; // CREATED, UPDATED, DELETED
    private Long feedId;
    private Long commentId;
    private FeedCommentResponseDTO comment;
}
