package com.polytech.interactionservice.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String receiverId;
    private Long transactionId;
    private Integer rating; // 1-5
    private String comment;
}