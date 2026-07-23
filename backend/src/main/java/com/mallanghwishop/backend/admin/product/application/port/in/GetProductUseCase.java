package com.mallanghwishop.backend.admin.product.application.port.in;

import com.mallanghwishop.backend.admin.product.application.result.ProductResult;

public interface GetProductUseCase {
    ProductResult getProduct(Long id);
}
