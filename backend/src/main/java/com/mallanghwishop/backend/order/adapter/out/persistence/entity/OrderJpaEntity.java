package com.mallanghwishop.backend.order.adapter.out.persistence.entity;

import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import com.mallanghwishop.backend.order.domain.model.OrderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "request_memo", length = 500)
    private String requestMemo;

    @Column(name = "order_uid", unique = true, length = 100)
    private String orderUid;

    @Column(name = "payment_key", length = 200)
    private String paymentKey;

    @Column(name = "coupon_id")
    private Long couponId;

    @Builder.Default
    @Column(name = "discount_amount")
    private Integer discountAmount = 0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderLineItemJpaEntity> items = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addLineItem(OrderLineItemJpaEntity item) {
        items.add(item);
        item.setOrder(this);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static OrderJpaEntity fromDomain(com.mallanghwishop.backend.order.domain.model.Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .totalPrice(order.getTotalPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .requestMemo(order.getRequestMemo())
                .orderUid(order.getOrderUid())
                .paymentKey(order.getPaymentKey())
                .couponId(order.getCouponId())
                .discountAmount(order.getDiscountAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
                
        if (order.getItems() != null) {
            entity.setItems(order.getItems().stream()
                    .map(item -> OrderLineItemJpaEntity.fromDomain(item, entity))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return entity;
    }

    public com.mallanghwishop.backend.order.domain.model.Order toDomain() {
        com.mallanghwishop.backend.order.domain.model.Order order = com.mallanghwishop.backend.order.domain.model.Order.builder()
                .id(this.id)
                .memberId(this.memberId)
                .totalPrice(this.totalPrice)
                .orderType(this.orderType)
                .status(this.status)
                .requestMemo(this.requestMemo)
                .orderUid(this.orderUid)
                .paymentKey(this.paymentKey)
                .couponId(this.couponId)
                .discountAmount(this.discountAmount)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
                
        if (this.items != null) {
            order.setItems(this.items.stream()
                    .map(OrderLineItemJpaEntity::toDomain)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return order;
    }
}
