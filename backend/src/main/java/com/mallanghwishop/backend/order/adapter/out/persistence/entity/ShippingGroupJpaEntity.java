package com.mallanghwishop.backend.order.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.mallanghwishop.backend.order.domain.model.ShippingGroup;

@Entity
@Table(name = "shipping_groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingGroupJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "refund_amount", nullable = false)
    private int refundAmount;

    @Column(name = "is_refunded", nullable = false)
    @Builder.Default
    private boolean isRefunded = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static ShippingGroupJpaEntity fromDomain(ShippingGroup group) {
        return ShippingGroupJpaEntity.builder()
                .id(group.getId())
                .memberId(group.getMemberId())
                .refundAmount(group.getRefundAmount())
                .isRefunded(group.isRefunded())
                .createdAt(group.getCreatedAt())
                .build();
    }

    public ShippingGroup toDomain() {
        return ShippingGroup.builder()
                .id(this.id)
                .memberId(this.memberId)
                .refundAmount(this.refundAmount)
                .isRefunded(this.isRefunded)
                .createdAt(this.createdAt)
                .build();
    }
}
