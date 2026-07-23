package com.mallanghwishop.backend.review.adapter.in.web.dto;

import com.mallanghwishop.backend.review.domain.model.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private String content;
    private int rating;
    private String nickname;
    private String productNames; // 주문한 상품 이름 (쉼표 구분)
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review, String nickname, String productNames) {
        return ReviewResponse.builder()
                .id(review.getId())
                .orderId(review.getOrderId())
                .content(review.getContent())
                .rating(review.getRating())
                .nickname(nickname)
                .productNames(productNames)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
