package com.mallanghwishop.backend.product.application.port.out;

import com.mallanghwishop.backend.product.domain.model.Product;

/**
 * 상품 저장 포트 (쓰기)
 */
public interface SaveProductPort {
    Product save(Product product);
    void deleteById(Long id);
}
