package com.mallanghwishop.backend.cart.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long id;
    @ToString.Exclude
    private Cart cart;
    private Long productId;
    private int quantity;
    private Integer unitPrice;  // 옵션 포함 단가 (null이면 상품 기본 가격 사용)
    private String selectedOptionNames;  // JSON 배열 문자열: ["Large", "ICE", "샷 추가"]
    private LocalDateTime createdAt;

}
