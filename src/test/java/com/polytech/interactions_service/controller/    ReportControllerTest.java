package com.polytech.interactions_service.controller;

import com.polytech.interactions_service.dto.ReportRequest;
import com.polytech.interactions_service.model.enums.ReportType;
import com.polytech.interactions_service.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private ReportService reportService;

    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportController = new ReportController(reportService);
    }

    private Jwt jwtWithSubject(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject)
                .build();
    }

    @Test
    void createReport_delegatesToService_andReturnsCreated() {
        ReportRequest request = new ReportRequest();
        request.setReportedUserId("target-user");
        request.setReportedTicketId(UUID.randomUUID());
        request.setReportedTransactionId(UUID.randomUUID());
        request.setReportType(ReportType.FRAUD);
        request.setReason("Bad behaviour");
        request.setEvidence("screenshot");

        Jwt jwt = jwtWithSubject("reporter-1");

        ResponseEntity<Void> response = reportController.createReport(request, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(reportService).createReport("reporter-1", request);
    }
}
