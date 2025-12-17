package com.mysite.devtallks.feed.event;

import com.mysite.devtallks.comment.dto.FeedCommentResponseDTO;
import com.mysite.devtallks.feed.dto.FeedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishFeedCreated(FeedResponseDTO feed) {
        sendToFeedList(FeedChangeEvent.builder().type("CREATED").feed(feed).build());
        sendToFeed(feed.getId(), FeedChangeEvent.builder().type("CREATED").feed(feed).build());
    }

    public void publishFeedUpdated(FeedResponseDTO feed) {
        sendToFeedList(FeedChangeEvent.builder().type("UPDATED").feed(feed).build());
        sendToFeed(feed.getId(), FeedChangeEvent.builder().type("UPDATED").feed(feed).build());
    }

    public void publishFeedDeleted(Long feedId) {
        FeedChangeEvent event = FeedChangeEvent.builder().type("DELETED").feedId(feedId).build();
        sendToFeedList(event);
        sendToFeed(feedId, event);
    }

    public void publishLikeUpdated(Long feedId, long likeCount, boolean liked) {
        FeedLikeEvent event = FeedLikeEvent.builder()
                .feedId(feedId)
                .likeCount(likeCount)
                .type(liked ? "LIKED" : "UNLIKED")
                .build();
        messagingTemplate.convertAndSend("/topic/feeds/" + feedId + "/likes", event);
    }

    public void publishCommentCreated(Long feedId, FeedCommentResponseDTO comment) {
        publishCommentEvent("CREATED", feedId, comment);
    }

    public void publishCommentUpdated(Long feedId, FeedCommentResponseDTO comment) {
        publishCommentEvent("UPDATED", feedId, comment);
    }

    public void publishCommentDeleted(Long feedId, Long commentId) {
        FeedCommentEvent event = FeedCommentEvent.builder()
                .type("DELETED")
                .feedId(feedId)
                .commentId(commentId)
                .build();
        messagingTemplate.convertAndSend("/topic/feeds/" + feedId + "/comments", event);
    }

    private void publishCommentEvent(String type, Long feedId, FeedCommentResponseDTO comment) {
        FeedCommentEvent event = FeedCommentEvent.builder()
                .type(type)
                .feedId(feedId)
                .commentId(comment.getId())
                .comment(comment)
                .build();
        messagingTemplate.convertAndSend("/topic/feeds/" + feedId + "/comments", event);
    }

    private void sendToFeedList(FeedChangeEvent event) {
        messagingTemplate.convertAndSend("/topic/feeds", event);
    }

    private void sendToFeed(Long feedId, FeedChangeEvent event) {
        messagingTemplate.convertAndSend("/topic/feeds/" + feedId, event);
    }
}
