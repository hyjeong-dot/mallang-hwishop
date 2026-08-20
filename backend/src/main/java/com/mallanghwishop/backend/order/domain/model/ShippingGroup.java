package com.mallanghwishop.backend.order.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ShippingGroup {
    private Long id;
    private UUID memberId;
    private int refundAmount;
    private boolean isRefunded;
    private LocalDateTime createdAt;
}
