package com.mallanghwishop.backend.product.adapter.out.persistence.repository;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.id = :id AND p.isAvailable = true")
    Optional<ProductJpaEntity> findAvailableByIdWithLock(@Param("id") Long id);

    List<ProductJpaEntity> findAllByIsAvailableTrueOrderBySortOrderAsc();

    List<ProductJpaEntity> findAllByIsAvailableTrueAndCategoryIdOrderBySortOrderAsc(Long categoryId);

    List<ProductJpaEntity> findAllByIsAvailableTrueAndKorNameContainingIgnoreCaseOrIsAvailableTrueAndDescriptionContainingIgnoreCaseOrderBySortOrderAsc(String korName, String description);

    List<ProductJpaEntity> findAllByIsAvailableTrueAndCategoryIdAndKorNameContainingIgnoreCaseOrIsAvailableTrueAndCategoryIdAndDescriptionContainingIgnoreCaseOrderBySortOrderAsc(Long categoryId, String korName, Long categoryId2, String description);

    Optional<ProductJpaEntity> findByIdAndIsAvailableTrue(Long id);

    Optional<ProductJpaEntity> findBySlugAndIsAvailableTrue(String slug);

    List<ProductJpaEntity> findAllByOrderBySortOrderAsc();

    Optional<ProductJpaEntity> findByKorName(String korName);

    boolean existsByKorName(String korName);

    List<ProductJpaEntity> findAllBySaleStatus(com.mallanghwishop.backend.product.domain.model.SaleStatus saleStatus);
}
