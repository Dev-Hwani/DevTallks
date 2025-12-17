package com.mysite.devtallks.feed.controller;

import com.mysite.devtallks.feed.dto.FeedEventMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FeedWebSocketController {

    @MessageMapping("/feeds/{feedId}/events")
    @SendTo("/topic/feeds/{feedId}")
    public FeedEventMessage broadcast(@DestinationVariable Long feedId, @Payload FeedEventMessage message) {
        // 기본 브로드캐스트 컨트롤러: 클라이언트 간 실시간 전파만 수행.
        return FeedEventMessage.builder()
                .feedId(feedId)
                .memberId(message.getMemberId())
                .memberNickname(message.getMemberNickname())
                .type(message.getType())
                .payload(message.getPayload())
                .build();
    }
}
