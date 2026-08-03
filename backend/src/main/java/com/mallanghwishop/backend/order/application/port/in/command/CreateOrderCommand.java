package com.mallanghwishop.backend.order.application.port.in.command;

import com.mallanghwishop.backend.order.domain.model.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateOrderCommand {
    private final String username;
    private final OrderType orderType;
    private final String requestMemo;
    private final Long couponId; // nullable
    private final Integer pointUsed; // nullable
    private final List<OrderLineItemCommand> items;
}
