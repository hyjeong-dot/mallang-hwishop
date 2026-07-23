package com.mallanghwishop.backend.admin.product.application.port.in;

import com.mallanghwishop.backend.admin.product.application.result.ProductListResult;

/**
 * 상품 목록 조회 유즈케이스 (관리자 전용)
 */
public interface GetProductListUseCase {
    ProductListResult getProducts(Long categoryId, String searchQuery);
}
