package com.polytech.interactions_service.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String receiverId;
    private Long transactionId; // Peut être null
    private String subject;
    private String content;
}