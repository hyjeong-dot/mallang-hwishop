package com.mallanghwishop.backend.product.adapter.out.persistence.repository;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductOptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOptionJpaEntity, Long> {

    List<ProductOptionJpaEntity> findAllByProductIdOrderBySortOrderAsc(Long productId);

    void deleteAllByProductId(Long productId);
}
