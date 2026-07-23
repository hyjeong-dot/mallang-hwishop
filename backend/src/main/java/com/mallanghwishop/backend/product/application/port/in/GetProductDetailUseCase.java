package com.mallanghwishop.backend.product.application.port.in;

import com.mallanghwishop.backend.product.application.result.ProductDetailResult;

/**
 * 상품 상세 조회 유스케이스 (사용자용)
 */
public interface GetProductDetailUseCase {
    ProductDetailResult getAvailableProduct(Long id);
    ProductDetailResult getAvailableProductBySlug(String slug);
}
