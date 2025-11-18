package com.polytech.interactionservice.controller;

import com.polytech.interactionservice.dto.MessageRequest;
import com.polytech.interactionservice.model.Message;
import com.polytech.interactionservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages") // L'URL finale sera /api/interactions/messages via la Gateway
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ENVOYER UN MESSAGE
    @PostMapping
    public ResponseEntity<Void> sendMessage(
            @RequestBody MessageRequest request,
            @AuthenticationPrincipal Jwt principal) { // Récupère le token JWT automatiquement
        
        // Le 'subject' du token est l'ID de l'utilisateur (Keycloak ID)
        String senderId = principal.getSubject();
        
        messageService.sendMessage(senderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // VOIR MES MESSAGES RECUS
    @GetMapping
    public ResponseEntity<List<Message>> getMyMessages(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        return ResponseEntity.ok(messageService.getMyInbox(userId));
    }
    
    // MARQUER COMME LU
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}