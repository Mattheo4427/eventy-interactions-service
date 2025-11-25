package com.polytech.interactions_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequest {
    private String receiverId;
    private UUID transactionId;
    private Integer rating; // 1-5
    private String comment;
}