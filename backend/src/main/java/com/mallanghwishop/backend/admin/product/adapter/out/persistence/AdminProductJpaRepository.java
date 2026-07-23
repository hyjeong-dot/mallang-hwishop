package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdminProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    List<ProductJpaEntity> findAllByOrderBySortOrderAsc();

    List<ProductJpaEntity> findAllByCategoryIdOrderBySortOrderAsc(Long categoryId);

    // LIKE 검색 -> 메서드 네이밍으로 변경
    List<ProductJpaEntity> findAllByKorNameContainingIgnoreCaseOrEngNameContainingIgnoreCaseOrderBySortOrderAsc(String korName, String engName);

    List<ProductJpaEntity> findAllByCategoryIdAndKorNameContainingIgnoreCaseOrCategoryIdAndEngNameContainingIgnoreCaseOrderBySortOrderAsc(Long categoryId, String korName, Long categoryId2, String engName);

    boolean existsByKorName(String korName);

    Optional<ProductJpaEntity> findByKorName(String korName);

    // slug 관련
    boolean existsBySlug(String slug);

    List<ProductJpaEntity> findAllBySlugIsNull();

    long countBySlugStartingWith(String slugPrefix);
}
