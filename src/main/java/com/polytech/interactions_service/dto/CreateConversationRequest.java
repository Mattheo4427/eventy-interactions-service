package com.polytech.interactions_service.dto;

import lombok.Data;

@Data
public class CreateConversationRequest {
    private String participantId;
    private String ticketId;
    private String eventId;
}
