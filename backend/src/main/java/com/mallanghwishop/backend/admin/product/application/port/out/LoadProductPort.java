package com.mallanghwishop.backend.admin.product.application.port.out;

import com.mallanghwishop.backend.admin.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * 상품 조회 포트 (영속성 계층 호출 인터페이스)
 */
public interface LoadProductPort {
    List<Product> findAll(Long categoryId, String searchQuery);
    Optional<Product> findById(Long id);
    boolean existsBySlug(String slug);
    List<Product> findAllBySlugIsNull();
    long countBySlugStartingWith(String slugPrefix);
}
