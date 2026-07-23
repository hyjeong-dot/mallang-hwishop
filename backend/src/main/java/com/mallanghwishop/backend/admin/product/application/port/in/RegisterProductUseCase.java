package com.mallanghwishop.backend.admin.product.application.port.in;

import com.mallanghwishop.backend.admin.product.application.command.RegisterProductCommand;

/**
 * 상품 등록 유즈케이스 (관리자 전용)
 */
public interface RegisterProductUseCase {
    Long registerProduct(RegisterProductCommand command);
}
