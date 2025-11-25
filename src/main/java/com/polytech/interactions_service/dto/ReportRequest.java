package com.polytech.interactions_service.dto;

import com.polytech.interactions_service.model.enums.ReportType;
import lombok.Data;

import java.util.UUID;

@Data
public class ReportRequest {
    private String reportedUserId;
    private UUID reportedTicketId;
    private UUID reportedTransactionId;
    private ReportType reportType;
    private String reason;
    private String evidence;
}