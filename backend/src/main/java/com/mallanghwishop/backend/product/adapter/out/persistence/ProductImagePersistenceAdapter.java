package com.mallanghwishop.backend.product.adapter.out.persistence;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductImageJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductImageJpaRepository;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.domain.model.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductImagePersistenceAdapter implements ProductImagePort {

    private final ProductImageJpaRepository repository;

    @Override
    public List<ProductImage> findAllByProductId(Long productId) {
        return repository.findAllByProductIdOrderBySortOrderAsc(productId)
                .stream().map(ProductImageJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public ProductImage save(ProductImage image) {
        return repository.save(ProductImageJpaEntity.fromDomain(image)).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllByProductId(Long productId) {
        repository.deleteAllByProductId(productId);
    }
}
