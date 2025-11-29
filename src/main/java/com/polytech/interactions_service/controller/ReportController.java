package com.polytech.interactions_service.controller;

import com.polytech.interactions_service.dto.ReportRequest;
import com.polytech.interactions_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.polytech.interactions_service.model.Report;
import com.polytech.interactions_service.model.enums.ReportStatus;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports") // URL Finale: /api/interactions/reports
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> createReport(
            @RequestBody ReportRequest request,
            @AuthenticationPrincipal Jwt principal) {
        
        String reporterId = principal.getSubject();
        reportService.createReport(reporterId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/admin/all")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Void> updateReportStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal Jwt principal) {
        
        String adminId = principal.getSubject();
        ReportStatus status = ReportStatus.valueOf(payload.get("status"));
        String adminAction = payload.get("adminAction");
        
        reportService.updateReportStatus(id, status, adminId, adminAction);
        return ResponseEntity.ok().build();
    }
}