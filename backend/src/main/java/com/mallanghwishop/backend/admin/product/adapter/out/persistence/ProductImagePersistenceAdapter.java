package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

import com.mallanghwishop.backend.admin.product.adapter.out.persistence.entity.AdminProductImageJpaEntity;
import java.util.stream.Collectors;

@Component("adminProductImagePersistenceAdapter")
@RequiredArgsConstructor
public class ProductImagePersistenceAdapter implements ProductImagePort {

    private final AdminProductImageJpaRepository repository;

    @Override
    public Long saveImage(AdminProductImage image) {
        return repository.save(AdminProductImageJpaEntity.fromDomain(image)).getId();
    }

    @Override
    public void deleteImage(Long imageId) {
        repository.deleteById(imageId);
    }

    @Override
    public Optional<AdminProductImage> findImageById(Long imageId) {
        return repository.findById(imageId).map(AdminProductImageJpaEntity::toDomain);
    }

    @Override
    public List<AdminProductImage> findAllByProductId(Long productId) {
        return repository.findAllByProductIdOrderBySortOrderAsc(productId).stream()
                .map(AdminProductImageJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByProductId(Long productId) {
        repository.deleteByProductId(productId);
    }

    @Override
    public void setPrimaryImage(Long productId, Long imageId) {
        // 이 상품의 모든 이미지를 일단 순서 1로 밀어냄 (비대표)
        List<AdminProductImageJpaEntity> allImages = repository.findAllByProductIdOrderBySortOrderAsc(productId);
        for (AdminProductImageJpaEntity img : allImages) {
            img.setSortOrder(1);
        }
        
        // 선택된 이미지만 0으로 설정 (대표)
        repository.findById(imageId).ifPresent(img -> {
            img.setSortOrder(0);
        });
        
        repository.saveAll(allImages);
    }
}
