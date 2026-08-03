package com.mallanghwishop.backend.product.application.port.out;

import com.mallanghwishop.backend.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * 상품 로드 포트 (읽기)
 */
public interface LoadProductPort {
    List<Product> findAllAvailable();
    List<Product> findAllAvailableByCategoryId(Long categoryId);
    List<Product> searchAvailable(String searchQuery);
    List<Product> searchAvailableByCategoryId(Long categoryId, String searchQuery);
    Optional<Product> findAvailableById(Long id);
    Optional<Product> findAvailableByIdWithLock(Long id);
    Optional<Product> findAvailableBySlug(String slug);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findAllUpcoming();
}
