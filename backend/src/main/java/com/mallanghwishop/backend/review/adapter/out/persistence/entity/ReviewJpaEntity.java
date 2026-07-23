package com.mallanghwishop.backend.review.adapter.out.persistence.entity;

import com.mallanghwishop.backend.review.domain.model.Review;
import com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private OrderJpaEntity order;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private int rating; // 1~5

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static ReviewJpaEntity fromDomain(com.mallanghwishop.backend.review.domain.model.Review review, OrderJpaEntity orderEntity) {
        return ReviewJpaEntity.builder()
                .id(review.getId())
                .memberId(review.getMemberId())
                .order(orderEntity)
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public com.mallanghwishop.backend.review.domain.model.Review toDomain() {
        return com.mallanghwishop.backend.review.domain.model.Review.builder()
                .id(this.id)
                .memberId(this.memberId)
                .order(this.order != null ? this.order.toDomain() : null)
                .content(this.content)
                .rating(this.rating)
                .createdAt(this.createdAt)
                .build();
    }
}
