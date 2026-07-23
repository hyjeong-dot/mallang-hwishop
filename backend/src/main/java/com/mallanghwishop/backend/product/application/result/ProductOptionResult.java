package com.mallanghwishop.backend.product.application.result;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResult {
    private Long id;
    private Long productId;
    private String name;
    private Boolean isRequired;
    private Boolean isMultiSelect;
    private Integer sortOrder;
    private List<OptionItemResult> items;
}
