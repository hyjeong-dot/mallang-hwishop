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
    private final String orderType;
    private final String requestMemo;
    private final List<AdminOrderLineItemResult> items;
    private final LocalDateTime createdAt;
}
