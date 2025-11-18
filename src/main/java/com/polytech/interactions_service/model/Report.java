package com.polytech.interactionservice.model;

import com.polytech.interactionservice.model.enums.ReportStatus;
import com.polytech.interactionservice.model.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "reporter_id", nullable = false)
    private String reporterId;

    @Column(name = "reported_user_id")
    private String reportedUserId;

    @Column(name = "reported_ticket_id")
    private Long reportedTicketId;

    @Column(name = "reported_transaction_id")
    private Long reportedTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String evidence;

    @CreationTimestamp
    @Column(name = "report_date", updatable = false)
    private LocalDateTime reportDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "admin_action", columnDefinition = "TEXT")
    private String adminAction;

    @Column(name = "admin_id")
    private String adminId;
}