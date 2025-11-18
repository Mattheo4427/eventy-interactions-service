package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.ReportRequest;
import com.polytech.interactions_service.model.Report;
import com.polytech.interactions_service.model.enums.ReportStatus;
import com.polytech.interactions_service.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public void createReport(String reporterId, ReportRequest request) {
        Report report = Report.builder()
                .reporterId(reporterId)
                .reportedUserId(request.getReportedUserId())
                .reportedTicketId(request.getReportedTicketId())
                .reportedTransactionId(request.getReportedTransactionId())
                .reportType(request.getReportType())
                .reason(request.getReason())
                .evidence(request.getEvidence())
                .status(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }
}