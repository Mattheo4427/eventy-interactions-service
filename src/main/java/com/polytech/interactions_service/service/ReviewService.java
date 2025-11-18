package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.ReviewRequest;
import com.polytech.interactions_service.model.Review;
import com.polytech.interactions_service.model.enums.ReviewStatus;
import com.polytech.interactions_service.model.enums.ReviewType;
import com.polytech.interactions_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Crée un nouvel avis lié à une transaction.
     */
    public void createReview(String authorId, ReviewRequest request) {
        // Note : Ici, on pourrait appeler le TransactionService (via FeignClient) 
        // pour vérifier que la transaction existe bien et que l'auteur en fait partie.

        Review review = Review.builder()
                .authorId(authorId)
                .receiverId(request.getReceiverId())
                .transactionId(request.getTransactionId())
                .rating(request.getRating())
                .comment(request.getComment())
                .status(ReviewStatus.PENDING) // Peut nécessiter une modération
                // Logique simple pour déterminer le type (à affiner selon vos règles métier)
                .reviewType(ReviewType.BUYER_TO_SELLER) 
                .build();

        reviewRepository.save(review);
    }

    /**
     * Récupère les avis reçus par un utilisateur spécifique.
     */
    public List<Review> getReviewsReceivedByUser(String userId) {
        return reviewRepository.findByReceiverId(userId);
    }
}