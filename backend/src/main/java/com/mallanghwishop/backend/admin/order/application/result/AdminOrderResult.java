package com.mallanghwishop.backend.admin.order.application.result;

import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class AdminOrderResult {
    private final Long id;
    private final String orderUid;
    private final UUID memberId;
    private final String name; // To show who ordered
    private final int totalPrice;
    private final String status;
    private final String statusLabel; // For UI display
    private final String recipientName;
    private final String phoneNumber;
    private final String zipcode;
    private final String address;
    private final String detailAddress;
    private final int pointUsed;
    private final int pointEarned;
    private final int deliveryFee;
    private final String requestMemo;
    private final String cancelReason;
    private final String cancelReasonType;
    private final String trackingCarrier;
    private final String trackingNumber;
    private final List<AdminOrderLineItemResult> items;
    private final LocalDateTime createdAt;
}
