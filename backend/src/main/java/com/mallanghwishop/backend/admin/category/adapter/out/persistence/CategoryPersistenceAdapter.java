package com.mallanghwishop.backend.admin.category.adapter.out.persistence;

import com.mallanghwishop.backend.admin.category.application.port.out.DeleteCategoryPort;
import com.mallanghwishop.backend.admin.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.admin.category.application.port.out.SaveCategoryPort;
import com.mallanghwishop.backend.admin.category.domain.model.AdminCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

import com.mallanghwishop.backend.admin.category.adapter.out.persistence.entity.AdminCategoryJpaEntity;
import java.util.stream.Collectors;

@Component("adminCategoryPersistenceAdapter")
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements SaveCategoryPort, LoadCategoryPort, DeleteCategoryPort {

    private final AdminCategoryJpaRepository categoryJpaRepository;

    @Override
    public Long save(AdminCategory category) {
        AdminCategoryJpaEntity entity = AdminCategoryJpaEntity.fromDomain(category);
        return categoryJpaRepository.save(entity).getId();
    }

    @Override
    public List<AdminCategory> findAll() {
        return categoryJpaRepository.findAllByOrderBySortOrderAsc().stream()
                .map(AdminCategoryJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AdminCategory> findById(Long id) {
        return categoryJpaRepository.findById(id).map(AdminCategoryJpaEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}
