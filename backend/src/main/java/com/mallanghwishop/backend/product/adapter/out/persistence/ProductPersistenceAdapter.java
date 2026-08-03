package com.mallanghwishop.backend.product.adapter.out.persistence;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductJpaRepository;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements LoadProductPort, SaveProductPort {

    private final ProductJpaRepository repository;

    @Override
    public List<Product> findAllAvailable() {
        return repository.findAllByIsAvailableTrueOrderBySortOrderAsc()
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllAvailableByCategoryId(Long categoryId) {
        return repository.findAllByIsAvailableTrueAndCategoryIdOrderBySortOrderAsc(categoryId)
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Product> searchAvailable(String searchQuery) {
        return repository.findAllByIsAvailableTrueAndKorNameContainingIgnoreCaseOrIsAvailableTrueAndDescriptionContainingIgnoreCaseOrderBySortOrderAsc(searchQuery, searchQuery)
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Product> searchAvailableByCategoryId(Long categoryId, String searchQuery) {
        return repository.findAllByIsAvailableTrueAndCategoryIdAndKorNameContainingIgnoreCaseOrIsAvailableTrueAndCategoryIdAndDescriptionContainingIgnoreCaseOrderBySortOrderAsc(categoryId, searchQuery, categoryId, searchQuery)
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findAvailableById(Long id) {
        return repository.findByIdAndIsAvailableTrue(id).map(ProductJpaEntity::toDomain);
    }

    @Override
    public Optional<Product> findAvailableByIdWithLock(Long id) {
        return repository.findAvailableByIdWithLock(id).map(ProductJpaEntity::toDomain);
    }

    @Override
    public Optional<Product> findAvailableBySlug(String slug) {
        return repository.findBySlugAndIsAvailableTrue(slug).map(ProductJpaEntity::toDomain);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAllByOrderBySortOrderAsc()
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllUpcoming() {
        return repository.findAllBySaleStatus(com.mallanghwishop.backend.product.domain.model.SaleStatus.UPCOMING)
                .stream().map(ProductJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        return repository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
