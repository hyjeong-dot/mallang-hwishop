package com.mallanghwishop.backend.order.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_line_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private OrderJpaEntity order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int price; // 구매 당시 가격

    @Column(nullable = false)
    private int quantity; // 구매 수량

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static OrderLineItemJpaEntity fromDomain(com.mallanghwishop.backend.order.domain.model.OrderLineItem item, OrderJpaEntity orderEntity) {
        return OrderLineItemJpaEntity.builder()
                .id(item.getId())
                .order(orderEntity)
                .productId(item.getProductId())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .createdAt(item.getCreatedAt())
                .build();
    }

    public com.mallanghwishop.backend.order.domain.model.OrderLineItem toDomain() {
        return com.mallanghwishop.backend.order.domain.model.OrderLineItem.builder()
                .id(this.id)
                .productId(this.productId)
                .price(this.price)
                .quantity(this.quantity)
                .createdAt(this.createdAt)
                .build();
    }
}
