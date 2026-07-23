package com.mallanghwishop.backend.admin.product.adapter.out.persistence;

import com.mallanghwishop.backend.admin.product.application.port.out.DeleteProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component("adminProductPersistenceAdapter")
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements SaveProductPort, LoadProductPort, DeleteProductPort {

    private final AdminProductJpaRepository productJpaRepository;

    @Override
    public Long save(Product product) {
        return productJpaRepository.save(product).getId();
    }

    @Override
    public List<Product> findAll(Long categoryId, String searchQuery) {
        boolean hasCategory = categoryId != null;
        boolean hasSearch = searchQuery != null && !searchQuery.isBlank();

        if (hasCategory && hasSearch) {
            return productJpaRepository.findAllByCategoryIdAndKorNameContainingIgnoreCaseOrCategoryIdAndEngNameContainingIgnoreCaseOrderBySortOrderAsc(categoryId, searchQuery, categoryId, searchQuery);
        } else if (hasCategory) {
            return productJpaRepository.findAllByCategoryIdOrderBySortOrderAsc(categoryId);
        } else if (hasSearch) {
            return productJpaRepository.findAllByKorNameContainingIgnoreCaseOrEngNameContainingIgnoreCaseOrderBySortOrderAsc(searchQuery, searchQuery);
        } else {
            return productJpaRepository.findAllByOrderBySortOrderAsc();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
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
        return productJpaRepository.findAllBySlugIsNull();
    }

    @Override
    public long countBySlugStartingWith(String slugPrefix) {
        return productJpaRepository.countBySlugStartingWith(slugPrefix);
    }
}
