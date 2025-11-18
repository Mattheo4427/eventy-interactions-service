package com.polytech.interactionservice.dto;

import com.polytech.interactionservice.model.enums.ReportType;
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