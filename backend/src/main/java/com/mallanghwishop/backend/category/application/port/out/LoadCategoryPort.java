package com.mallanghwishop.backend.category.application.port.out;

import com.mallanghwishop.backend.category.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface LoadCategoryPort {
    List<Category> findAllActive();
    Optional<Category> findById(Long id);
}
