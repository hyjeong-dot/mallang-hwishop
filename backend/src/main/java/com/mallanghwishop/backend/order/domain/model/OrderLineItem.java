package com.mallanghwishop.backend.order.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItem {

    private Long id;
    @ToString.Exclude
    private Order order;
    private Long productId;
    private int price; // 구매 당시 가격
    private int quantity; // 구매 수량
    private LocalDateTime createdAt;
}
