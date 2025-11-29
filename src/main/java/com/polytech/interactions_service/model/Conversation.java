package com.polytech.interactions_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    private String id;

    @Column(name = "participant1_id", nullable = false)
    private String participant1Id;

    @Column(name = "participant2_id", nullable = false)
    private String participant2Id;

    @Column(name = "related_ticket_id")
    private String relatedTicketId;

    @Column(name = "related_event_id")
    private String relatedEventId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
