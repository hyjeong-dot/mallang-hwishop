package com.mallanghwishop.backend.order.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    private UUID memberId;
    private int totalPrice;
    private OrderType orderType;
    private OrderStatus status;
    private String requestMemo;
    private String orderUid;
    private String paymentKey;
    private Long couponId;
    
    @Builder.Default
    private int pointUsed = 0;
    
    @Builder.Default
    private int pointEarned = 0;

    @Builder.Default
    private Integer discountAmount = 0;

    @Builder.Default
    private List<OrderLineItem> items = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addLineItem(OrderLineItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public void applyDiscount(int discount) {
        this.discountAmount = discount;
        this.totalPrice = Math.max(0, this.totalPrice - discount);
    }
}
