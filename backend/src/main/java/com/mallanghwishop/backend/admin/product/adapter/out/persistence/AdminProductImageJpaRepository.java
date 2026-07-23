package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.adapter.out.persistence.entity.AdminProductImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminProductImageJpaRepository extends JpaRepository<AdminProductImageJpaEntity, Long> {
    List<AdminProductImageJpaEntity> findAllByProductIdOrderBySortOrderAsc(Long productId);
    void deleteByProductId(Long productId);
}
