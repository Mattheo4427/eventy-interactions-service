package com.polytech.interactionservice.controller;

import com.polytech.interactionservice.dto.ReportRequest;
import com.polytech.interactionservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    
    // Note: Les méthodes GET pour lister les reports seraient réservées aux admins
}