package com.mallanghwishop.backend.point.adapter.out.persistence.entity;

import com.mallanghwishop.backend.point.domain.model.PointHistory;
import com.mallanghwishop.backend.point.domain.model.PointType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "point_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PointHistoryJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;

    private Long orderId;

    private String description;

    private LocalDateTime expireAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static PointHistoryJpaEntity fromDomain(PointHistory domain) {
        if (domain == null) return null;
        return PointHistoryJpaEntity.builder()
                .id(domain.getId())
                .memberId(domain.getMemberId())
                .amount(domain.getAmount())
                .type(domain.getType())
                .orderId(domain.getOrderId())
                .description(domain.getDescription())
                .expireAt(domain.getExpireAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public PointHistory toDomain() {
        return PointHistory.builder()
                .id(this.id)
                .memberId(this.memberId)
                .amount(this.amount)
                .type(this.type)
                .orderId(this.orderId)
                .description(this.description)
                .expireAt(this.expireAt)
                .createdAt(this.createdAt)
                .build();
    }
}
