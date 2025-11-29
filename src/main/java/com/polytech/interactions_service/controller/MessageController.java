package com.polytech.interactions_service.controller;

import com.polytech.interactions_service.dto.ConversationResponse;
import com.polytech.interactions_service.dto.CreateConversationRequest;
import com.polytech.interactions_service.dto.MessageRequest;
import com.polytech.interactions_service.dto.SendMessageRequest;
import com.polytech.interactions_service.model.Conversation;
import com.polytech.interactions_service.model.Message;
import com.polytech.interactions_service.service.MessageService;
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

    // --- CONVERSATIONS ---

    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversation(
            @RequestBody CreateConversationRequest request,
            @AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        return ResponseEntity.ok(messageService.createConversation(userId, request));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getMyConversations(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        return ResponseEntity.ok(messageService.getMyConversations(userId));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<Message>> getConversationMessages(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        return ResponseEntity.ok(messageService.getConversationMessages(id, userId));
    }

    @PostMapping("/conversations/{id}/messages")
    public ResponseEntity<Void> sendMessageToConversation(
            @PathVariable String id,
            @RequestBody SendMessageRequest request,
            @AuthenticationPrincipal Jwt principal) {
        String userId = principal.getSubject();
        messageService.sendMessageToConversation(userId, id, request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --- LEGACY / DIRECT MESSAGES ---

    // ENVOYER UN MESSAGE (Legacy)
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