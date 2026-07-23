package com.mallanghwishop.backend.admin.product.application.port.out;

import com.mallanghwishop.backend.admin.product.domain.model.Product;

/**
 * 상품 저장 포트 (영속성 계층 호출 인터페이스)
 */
public interface SaveProductPort {
    Long save(Product product);
}
