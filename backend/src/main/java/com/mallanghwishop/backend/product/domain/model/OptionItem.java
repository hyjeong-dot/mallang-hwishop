package com.mallanghwishop.backend.product.domain.model;

import lombok.*;

/**
 * 옵션 항목 도메인 모델 (순수 POJO)
 * - 하나의 옵션 그룹에 여러 항목이 들어감 (1:N)
 * - 예: "사이즈 선택" → Small(+0), Large(+500)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionItem {

    private Long id;
    private Long optionId;
    private String name;
    private Integer priceDelta;
    private Integer sortOrder;
}
