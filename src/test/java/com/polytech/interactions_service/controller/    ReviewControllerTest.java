package com.polytech.interactions_service.controller;

import com.polytech.interactions_service.dto.ReviewRequest;
import com.polytech.interactions_service.model.Review;
import com.polytech.interactions_service.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewController = new ReviewController(reviewService);
    }

    private Jwt jwtWithSubject(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject)
                .build();
    }

    @Test
    void createReview_delegatesToService_andReturnsCreated() {
        ReviewRequest request = new ReviewRequest();
        request.setReceiverId("seller-1");
        request.setTransactionId(UUID.randomUUID());
        request.setRating(4);
        request.setComment("Good overall");

        Jwt jwt = jwtWithSubject("buyer-1");

        ResponseEntity<Void> response = reviewController.createReview(request, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(reviewService).createReview("buyer-1", request);
    }

    @Test
    void getUserReviews_returnsBodyFromService() {
        List<Review> reviews = List.of(Review.builder().build());
        when(reviewService.getReviewsReceivedByUser("user-9")).thenReturn(reviews);

        ResponseEntity<List<Review>> response = reviewController.getUserReviews("user-9");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reviews);
        verify(reviewService).getReviewsReceivedByUser("user-9");
    }
}
