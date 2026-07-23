package com.mallanghwishop.backend.product.domain.model;

import lombok.*;

/**
 * 상품 옵션 그룹 도메인 모델 (순수 POJO)
 * - 하나의 상품에 여러 옵션 그룹이 붙을 수 있음 (1:N)
 * - 예: "사이즈 선택", "색상", "향 추가" 등
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {

    private Long id;
    private Long productId;
    private String name;
    private Boolean isRequired;
    private Boolean isMultiSelect;
    private Integer sortOrder;
}
