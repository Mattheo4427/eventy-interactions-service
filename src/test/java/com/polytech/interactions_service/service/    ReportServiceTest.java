package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.ReportRequest;
import com.polytech.interactions_service.model.Report;
import com.polytech.interactions_service.model.enums.ReportStatus;
import com.polytech.interactions_service.model.enums.ReportType;
import com.polytech.interactions_service.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReport_buildsAndSavesPendingReport() {
        ReportRequest request = new ReportRequest();
        UUID ticketId = UUID.randomUUID();
        UUID txId = UUID.randomUUID();

        request.setReportedUserId("reported-user");
        request.setReportedTicketId(ticketId);
        request.setReportedTransactionId(txId);
        request.setReportType(ReportType.FRAUD);
        request.setReason("Suspicious activity");
        request.setEvidence("screenshot");

        ArgumentCaptor<Report> captor = ArgumentCaptor.forClass(Report.class);
        when(reportRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reportService.createReport("reporter-1", request);

        Report saved = captor.getValue();
        assertThat(saved.getReporterId()).isEqualTo("reporter-1");
        assertThat(saved.getReportedUserId()).isEqualTo("reported-user");
        assertThat(saved.getReportedTicketId()).isEqualTo(ticketId);
        assertThat(saved.getReportedTransactionId()).isEqualTo(txId);
        assertThat(saved.getReportType()).isEqualTo(ReportType.FRAUD);
        assertThat(saved.getReason()).isEqualTo("Suspicious activity");
        assertThat(saved.getEvidence()).isEqualTo("screenshot");
        assertThat(saved.getStatus()).isEqualTo(ReportStatus.PENDING);
    }
}
