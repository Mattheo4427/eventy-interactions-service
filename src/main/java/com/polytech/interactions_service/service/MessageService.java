package com.polytech.interactionservice.service;

import com.polytech.interactionservice.dto.MessageRequest;
import com.polytech.interactionservice.model.Message;
import com.polytech.interactionservice.model.enums.MessageType;
import com.polytech.interactionservice.repository.MessageRepository;
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