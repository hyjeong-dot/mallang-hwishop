package com.mallanghwishop.backend.category.adapter.out.persistence;

import com.mallanghwishop.backend.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.category.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

import com.mallanghwishop.backend.category.adapter.out.persistence.entity.CategoryJpaEntity;
import java.util.stream.Collectors;

@Component("publicCategoryPersistenceAdapter")
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements LoadCategoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> findAllActive() {
        return categoryJpaRepository.findAllByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(CategoryJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id).map(CategoryJpaEntity::toDomain);
    }
}
