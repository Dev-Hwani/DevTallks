package com.mysite.devtallks.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedEventMessage {
    private Long feedId;
    private Long memberId;
    private String memberNickname;
    private String type;
    private String payload;
}
