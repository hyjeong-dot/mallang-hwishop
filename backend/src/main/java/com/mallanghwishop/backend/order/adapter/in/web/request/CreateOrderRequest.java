package com.mallanghwishop.backend.order.adapter.in.web.request;

import com.mallanghwishop.backend.order.application.port.in.command.CreateOrderCommand;
import com.mallanghwishop.backend.order.application.port.in.command.OrderLineItemCommand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CreateOrderRequest {
    private String recipientName;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String detailAddress;
    
    private String requestMemo;
    private Long couponId; // nullable
    private Integer pointUsed; // 사용한 적립금
    private List<OrderLineItemRequest> items;

    public CreateOrderCommand toCommand(String username) {
        return CreateOrderCommand.builder()
                .username(username)
                .recipientName(this.recipientName)
                .phoneNumber(this.phoneNumber)
                .zipcode(this.zipcode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .requestMemo(this.requestMemo)
                .couponId(this.couponId)
                .pointUsed(this.pointUsed)
                .items(this.items.stream()
                        .map(item -> OrderLineItemCommand.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
