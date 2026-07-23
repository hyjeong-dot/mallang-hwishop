package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminProductImageJpaRepository extends JpaRepository<AdminProductImage, Long> {
    List<AdminProductImage> findAllByProductIdOrderBySortOrderAsc(Long productId);
    void deleteByProductId(Long productId);
}
