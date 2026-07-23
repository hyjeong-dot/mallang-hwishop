package com.mallanghwishop.backend.category.adapter.out.persistence;

import com.mallanghwishop.backend.category.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    // 활성 상태이면서 정렬 순서대로 조회
    List<Category> findAllByIsActiveTrueOrderBySortOrderAsc();
}
