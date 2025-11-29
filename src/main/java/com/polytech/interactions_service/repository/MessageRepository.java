package com.polytech.interactions_service.repository;

import com.polytech.interactions_service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Trouver tous les messages reçus par un utilisateur
    List<Message> findByReceiverIdOrderByDateSentDesc(String receiverId);
    
    // Trouver la conversation entre deux personnes (optionnel, plus complexe)
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);

    List<Message> findByConversationIdOrderByDateSentAsc(String conversationId);
}



