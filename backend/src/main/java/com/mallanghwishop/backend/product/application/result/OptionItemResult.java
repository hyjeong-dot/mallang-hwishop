package com.mallanghwishop.backend.product.application.result;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionItemResult {
    private Long id;
    private Long optionId;
    private String name;
    private Integer priceDelta;
    private Integer sortOrder;
}
