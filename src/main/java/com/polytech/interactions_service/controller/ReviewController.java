package com.polytech.interactionservice.controller;

import com.polytech.interactionservice.dto.ReviewRequest;
import com.polytech.interactionservice.model.Review;
import com.polytech.interactionservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews") // URL Finale: /api/interactions/reviews
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal Jwt principal) {
        
        String authorId = principal.getSubject();
        reviewService.createReview(authorId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint public (ou authentifié) pour voir les avis sur un vendeur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable String userId) {
        return ResponseEntity.ok(reviewService.getReviewsReceivedByUser(userId));
    }
}