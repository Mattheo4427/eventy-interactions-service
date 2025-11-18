package com.polytech.interactionservice.repository;

import com.polytech.interactionservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Trouver tous les messages reçus par un utilisateur
    List<Message> findByReceiverIdOrderByDateSentDesc(String receiverId);
    
    // Trouver la conversation entre deux personnes (optionnel, plus complexe)
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
}

// --- ReviewRepository.java ---
package com.polytech.interactionservice.repository;

import com.polytech.interactionservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReceiverId(String receiverId);
}

// --- ReportRepository.java ---
package com.polytech.interactionservice.repository;

import com.polytech.interactionservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}