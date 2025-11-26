package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.ReviewRequest;
import com.polytech.interactions_service.model.Review;
import com.polytech.interactions_service.model.enums.ReviewStatus;
import com.polytech.interactions_service.model.enums.ReviewType;
import com.polytech.interactions_service.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_buildsAndSavesPendingBuyerToSellerReview() {
        ReviewRequest request = new ReviewRequest();
        UUID txId = UUID.randomUUID();

        request.setReceiverId("seller-1");
        request.setTransactionId(txId);
        request.setRating(5);
        request.setComment("Great experience");

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        when(reviewRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.createReview("buyer-1", request);

        Review saved = captor.getValue();
        assertThat(saved.getAuthorId()).isEqualTo("buyer-1");
        assertThat(saved.getReceiverId()).isEqualTo("seller-1");
        assertThat(saved.getTransactionId()).isEqualTo(txId);
        assertThat(saved.getRating()).isEqualTo(5);
        assertThat(saved.getComment()).isEqualTo("Great experience");
        assertThat(saved.getStatus()).isEqualTo(ReviewStatus.PENDING);
        assertThat(saved.getReviewType()).isEqualTo(ReviewType.BUYER_TO_SELLER);
    }

    @Test
    void getReviewsReceivedByUser_returnsListFromRepository() {
        Review review = Review.builder().build();
        when(reviewRepository.findByReceiverId("user-1"))
                .thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsReceivedByUser("user-1");

        assertThat(result).hasSize(1).containsExactly(review);
        verify(reviewRepository).findByReceiverId("user-1");
    }
}
