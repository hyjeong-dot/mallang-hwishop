package com.mallanghwishop.backend.product.application.port.out;

import com.mallanghwishop.backend.product.domain.model.ProductImage;
import java.util.List;

/**
 * 상품 이미지 포트
 */
public interface ProductImagePort {
    List<ProductImage> findAllByProductId(Long productId);
    ProductImage save(ProductImage image);
    void deleteById(Long id);
    void deleteAllByProductId(Long productId);
}
