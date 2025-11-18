package com.polytech.interactionservice.model;

import com.polytech.interactionservice.model.enums.ReviewStatus;
import com.polytech.interactionservice.model.enums.ReviewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType;
}
