package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.ConversationResponse;
import com.polytech.interactions_service.dto.CreateConversationRequest;
import com.polytech.interactions_service.dto.MessageRequest;
import com.polytech.interactions_service.model.Conversation;
import com.polytech.interactions_service.model.Message;
import com.polytech.interactions_service.model.enums.MessageType;
import com.polytech.interactions_service.repository.ConversationRepository;
import com.polytech.interactions_service.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    @Transactional
    public Conversation createConversation(String userId, CreateConversationRequest request) {
        // Vérifier si une conversation existe déjà
        Optional<Conversation> existing = conversationRepository.findByParticipants(userId, request.getParticipantId());
        if (existing.isPresent()) {
            return existing.get();
        }

        Conversation conversation = Conversation.builder()
                .id(UUID.randomUUID().toString())
                .participant1Id(userId)
                .participant2Id(request.getParticipantId())
                .relatedTicketId(request.getTicketId())
                .relatedEventId(request.getEventId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return conversationRepository.save(conversation);
    }

    public List<ConversationResponse> getMyConversations(String userId) {
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        
        return conversations.stream().map(c -> {
            // Récupérer le dernier message
            List<Message> messages = messageRepository.findByConversationIdOrderByDateSentAsc(c.getId());
            Message lastMsg = messages.isEmpty() ? null : messages.get(messages.size() - 1);
            
            // Count unread messages for this user
            int unreadCount = (int) messages.stream()
                    .filter(m -> !m.isRead() && m.getReceiverId().equals(userId))
                    .count();

            return ConversationResponse.builder()
                    .id(c.getId())
                    .participant1Id(c.getParticipant1Id())
                    .participant2Id(c.getParticipant2Id())
                    .relatedTicketId(c.getRelatedTicketId())
                    .relatedEventId(c.getRelatedEventId())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .lastMessage(lastMsg)
                    .unreadCount(unreadCount)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<Message> getConversationMessages(String conversationId, String userId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByDateSentAsc(conversationId);
        
        // Mark unread messages as read for the receiver
        boolean updated = false;
        for (Message msg : messages) {
            if (!msg.isRead() && msg.getReceiverId().equals(userId)) {
                msg.setRead(true);
                updated = true;
            }
        }
        
        if (updated) {
            messageRepository.saveAll(messages);
        }
        
        return messages;
    }

    @Transactional
    public void sendMessage(String senderId, MessageRequest request) {
        // Si conversationId est fourni dans la requête (via URL ou body), on l'utilise
        // Sinon on essaie de le trouver ou créer
        
        // Note: MessageRequest a été conçu avant Conversation. 
        // Idéalement on devrait passer conversationId.
        // Ici on va supposer que le frontend appelle d'abord createConversation pour avoir l'ID,
        // OU on le déduit.
        
        // Pour simplifier, on va chercher la conversation existante entre sender et receiver
        String conversationId = null;
        Optional<Conversation> conv = conversationRepository.findByParticipants(senderId, request.getReceiverId());
        
        if (conv.isPresent()) {
            conversationId = conv.get().getId();
            // Update timestamp
            Conversation c = conv.get();
            c.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(c);
        } else {
            // Création automatique si pas de conversation (fallback)
            Conversation newConv = Conversation.builder()
                    .id(UUID.randomUUID().toString())
                    .participant1Id(senderId)
                    .participant2Id(request.getReceiverId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            conversationRepository.save(newConv);
            conversationId = newConv.getId();
        }

        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .transactionId(request.getTransactionId())
                .subject(request.getSubject())
                .content(request.getContent())
                .messageType(MessageType.GENERAL)
                .isRead(false)
                .conversationId(conversationId)
                .build();

        messageRepository.save(message);
    }
    
    @Transactional
    public void sendMessageToConversation(String senderId, String conversationId, String content) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        
        String receiverId = conv.getParticipant1Id().equals(senderId) ? conv.getParticipant2Id() : conv.getParticipant1Id();
        
        conv.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conv);

        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .subject("Message") // Default
                .messageType(MessageType.GENERAL)
                .isRead(false)
                .conversationId(conversationId)
                .relatedTicketId(conv.getRelatedTicketId())
                .relatedEventId(conv.getRelatedEventId())
                .build();

        messageRepository.save(message);
    }

    public List<Message> getMyInbox(String userId) {
        return messageRepository.findByReceiverIdOrderByDateSentDesc(userId);
    }
    
    public void markAsRead(Long messageId) {
         messageRepository.findById(messageId).ifPresent(msg -> {
             msg.setRead(true);
             messageRepository.save(msg);
         });
    }
}