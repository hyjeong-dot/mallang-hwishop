package com.mallanghwishop.backend.admin.product.application.port.in;

/**
 * 상품 삭제 유즈케이스 (관리자 전용)
 */
public interface DeleteProductUseCase {
    void deleteProduct(Long id);
}
