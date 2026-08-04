package com.mallanghwishop.backend.order.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderLineItemResult {
    private final Long productId;
    private final String productName;   // 상품 한글명
    private final String imageSrc;   // 상품 대표 이미지
    private final int price;         // 구매 당시 단가
    private final int quantity;
}
