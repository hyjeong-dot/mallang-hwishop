package com.mallanghwishop.backend.product.application.result;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResult {
    private List<ProductResult> products;
    private int productCount;
}
