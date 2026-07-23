package com.mallanghwishop.backend.product.application.port.in;

import com.mallanghwishop.backend.product.application.result.ProductImageListResult;

/**
 * 상품 이미지 목록 조회 유스케이스 (사용자용)
 */
public interface GetProductImageListUseCase {
    ProductImageListResult getProductImages(Long productId);
}
