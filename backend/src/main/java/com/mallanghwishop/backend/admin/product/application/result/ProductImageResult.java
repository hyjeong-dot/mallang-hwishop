package com.mallanghwishop.backend.admin.product.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageResult {
    private final Long id;
    private final Long productId;
    private final String srcUrl;
    private final Integer sortOrder;
}
