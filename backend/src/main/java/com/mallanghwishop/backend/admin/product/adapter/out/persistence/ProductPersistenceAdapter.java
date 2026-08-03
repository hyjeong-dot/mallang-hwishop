package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.application.port.out.DeleteProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("adminProductPersistenceAdapter")
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements SaveProductPort, LoadProductPort, DeleteProductPort {

    private final AdminProductJpaRepository productJpaRepository;

    private ProductJpaEntity toEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getId())
                .korName(product.getKorName())
                .engName(product.getEngName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .categoryId(product.getCategoryId())
                .isAvailable(product.getIsAvailable())
                .isSoldOut(product.getIsSoldOut())
                .sortOrder(product.getSortOrder())
                .saleStartAt(product.getSaleStartAt())
                .saleStatus(product.getSaleStatus())
                .stock(product.getStock())
                .maxPerOrder(product.getMaxPerOrder())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private Product toDomain(ProductJpaEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .korName(entity.getKorName())
                .engName(entity.getEngName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .discountPrice(entity.getDiscountPrice())
                .categoryId(entity.getCategoryId())
                .isAvailable(entity.getIsAvailable())
                .isSoldOut(entity.getIsSoldOut())
                .sortOrder(entity.getSortOrder())
                .saleStartAt(entity.getSaleStartAt())
                .saleStatus(entity.getSaleStatus())
                .stock(entity.getStock())
                .maxPerOrder(entity.getMaxPerOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public Long save(Product product) {
        ProductJpaEntity entity = toEntity(product);
        return productJpaRepository.save(entity).getId();
    }

    @Override
    public List<Product> findAll(Long categoryId, String searchQuery) {
        boolean hasCategory = categoryId != null;
        boolean hasSearch = searchQuery != null && !searchQuery.isBlank();
        List<ProductJpaEntity> entities;

        if (hasCategory && hasSearch) {
            entities = productJpaRepository.findAllByCategoryIdAndKorNameContainingIgnoreCaseOrCategoryIdAndEngNameContainingIgnoreCaseOrderBySortOrderAsc(categoryId, searchQuery, categoryId, searchQuery);
        } else if (hasCategory) {
            entities = productJpaRepository.findAllByCategoryIdOrderBySortOrderAsc(categoryId);
        } else if (hasSearch) {
            entities = productJpaRepository.findAllByKorNameContainingIgnoreCaseOrEngNameContainingIgnoreCaseOrderBySortOrderAsc(searchQuery, searchQuery);
        } else {
            entities = productJpaRepository.findAllByOrderBySortOrderAsc();
        }
        
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return productJpaRepository.existsBySlug(slug);
    }

    @Override
    public List<Product> findAllBySlugIsNull() {
        return productJpaRepository.findAllBySlugIsNull().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countBySlugStartingWith(String slugPrefix) {
        return productJpaRepository.countBySlugStartingWith(slugPrefix);
    }
}
