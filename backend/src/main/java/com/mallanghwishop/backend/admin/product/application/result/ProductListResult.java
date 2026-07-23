package com.mallanghwishop.backend.admin.product.application.result;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ProductListResult {
    private final List<ProductResult> products;
    private final int productCount;
}
