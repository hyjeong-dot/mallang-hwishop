package com.mallanghwishop.backend.product.adapter.out.persistence.repository;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductImageJpaRepository extends JpaRepository<ProductImageJpaEntity, Long> {

    List<ProductImageJpaEntity> findAllByProductIdOrderBySortOrderAsc(Long productId);

    void deleteAllByProductId(Long productId);
}
