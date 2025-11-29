package com.polytech.interactions_service.dto;

import com.polytech.interactions_service.model.Message;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationResponse {
    private String id;
    private String participant1Id;
    private String participant2Id;
    private String relatedTicketId;
    private String relatedEventId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Message lastMessage;
    private int unreadCount;
}
