package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdminProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderBySortOrderAsc();

    List<Product> findAllByCategoryIdOrderBySortOrderAsc(Long categoryId);

    // LIKE 검색 -> 메서드 네이밍으로 변경
    List<Product> findAllByKorNameContainingIgnoreCaseOrEngNameContainingIgnoreCaseOrderBySortOrderAsc(String korName, String engName);

    List<Product> findAllByCategoryIdAndKorNameContainingIgnoreCaseOrCategoryIdAndEngNameContainingIgnoreCaseOrderBySortOrderAsc(Long categoryId, String korName, Long categoryId2, String engName);

    boolean existsByKorName(String korName);

    Optional<Product> findByKorName(String korName);

    // slug 관련
    boolean existsBySlug(String slug);

    List<Product> findAllBySlugIsNull();

    long countBySlugStartingWith(String slugPrefix);
}
