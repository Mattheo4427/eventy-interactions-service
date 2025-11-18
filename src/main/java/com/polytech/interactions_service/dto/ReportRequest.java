package com.polytech.interactions_service.dto;

import com.polytech.interactions_service.model.enums.ReportType;
import lombok.Data;

@Data
public class ReportRequest {
    private String reportedUserId;
    private Long reportedTicketId;
    private Long reportedTransactionId;
    private ReportType reportType;
    private String reason;
    private String evidence;
}