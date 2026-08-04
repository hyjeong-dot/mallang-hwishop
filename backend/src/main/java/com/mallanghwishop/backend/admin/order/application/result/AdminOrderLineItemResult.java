package com.mallanghwishop.backend.admin.order.application.result;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class AdminOrderLineItemResult {
    private final Long productId;
    private final String productName;
    private final int price;
    private final int quantity;
}
