package com.mallanghwishop.backend.admin.product.application.port.in;

import com.mallanghwishop.backend.admin.product.application.command.UpdateProductCommand;

/**
 * 상품 수정 유즈케이스 (관리자 전용)
 */
public interface UpdateProductUseCase {
    void updateProduct(UpdateProductCommand command);
    void toggleSoldOut(Long id);
}
