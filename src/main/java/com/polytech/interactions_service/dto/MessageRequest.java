package com.polytech.interactions_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageRequest {
    private String receiverId;
    private UUID transactionId; // Peut être null
    private String subject;
    private String content;
}