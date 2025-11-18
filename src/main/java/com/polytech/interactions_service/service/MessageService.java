package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.MessageRequest;
import com.polytech.interactions_service.model.Message;
import com.polytech.interactions_service.model.enums.MessageType;
import com.polytech.interactions_service.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void sendMessage(String senderId, MessageRequest request) {
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .transactionId(request.getTransactionId())
                .subject(request.getSubject())
                .content(request.getContent())
                .messageType(MessageType.GENERAL) // Par défaut
                .isRead(false)
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