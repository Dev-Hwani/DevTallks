package com.mysite.devtallks.feed.event;

import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedChangeEvent {
    private String type; // CREATED, UPDATED, DELETED
    private FeedResponseDTO feed;
    private Long feedId; // populated for deleted events when feed null
}
