package com.mallanghwishop.backend.order.application.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateOrderCommand {
    private final String username;
    private final String recipientName;
    private final String phoneNumber;
    private final String zipcode;
    private final String address;
    private final String detailAddress;
    
    private final String requestMemo;
    private final Long couponId; // nullable
    private final Integer pointUsed; // nullable
    private final List<OrderLineItemCommand> items;
}
