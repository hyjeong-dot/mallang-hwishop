package com.mallanghwishop.backend.cart.adapter.in.web.dto;

import com.mallanghwishop.backend.cart.application.command.AddCartItemCommand;
import lombok.Data;
import java.util.List;
import java.util.UUID;

/**
 * 장바구니 아이템 추가 요청 DTO
 */
@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
    private Integer unitPrice;
    private List<String> selectedOptionNames;

    public AddCartItemCommand toCommand(UUID memberId) {
        return AddCartItemCommand.builder()
                .memberId(memberId)
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .selectedOptionNames(selectedOptionNames)
                .build();
    }
}
