package com.mallanghwishop.backend.admin.product.application.port.out;

import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import java.util.Optional;

public interface ProductImagePort {
    Long saveImage(AdminProductImage image);
    void deleteImage(Long imageId);
    Optional<AdminProductImage> findImageById(Long imageId);
    java.util.List<AdminProductImage> findAllByProductId(Long productId);
    void deleteByProductId(Long productId);
    void setPrimaryImage(Long productId, Long imageId);
}
