package com.mallanghwishop.backend.product.application.port.in;

import com.mallanghwishop.backend.product.application.result.ProductListResult;

/**
 * 상품 목록 조회 유스케이스 (사용자용)
 */
public interface GetProductListUseCase {
    ProductListResult getAvailableProducts(Long categoryId, String searchQuery);
}
