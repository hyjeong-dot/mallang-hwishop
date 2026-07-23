package com.mallanghwishop.backend.admin.category.adapter.out.persistence;

import com.mallanghwishop.backend.admin.category.adapter.out.persistence.entity.AdminCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface AdminCategoryJpaRepository extends JpaRepository<AdminCategoryJpaEntity, Long> {
    List<AdminCategoryJpaEntity> findAllByOrderBySortOrderAsc();
    Optional<AdminCategoryJpaEntity> findByName(String name);
}
