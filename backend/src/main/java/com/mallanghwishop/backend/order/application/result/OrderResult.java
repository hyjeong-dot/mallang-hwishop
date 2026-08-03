package com.mallanghwishop.backend.order.application.result;

import com.mallanghwishop.backend.order.domain.model.OrderStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResult {
    private final Long orderId;
    private final String orderUid;
    private final int totalPrice;
    private final String recipientName;
    private final String phoneNumber;
    private final String zipcode;
    private final String address;
    private final String detailAddress;
    private final OrderStatus status;
    private final LocalDateTime createdAt;
    private final List<OrderLineItemResult> items;
    private final String requestMemo;
}

