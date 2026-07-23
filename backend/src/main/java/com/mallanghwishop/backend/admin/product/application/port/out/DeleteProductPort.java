package com.mallanghwishop.backend.admin.product.application.port.out;

/**
 * 상품 삭제 포트 (영속성 계층 호출 인터페이스)
 */
public interface DeleteProductPort {
    void deleteById(Long id);
}
