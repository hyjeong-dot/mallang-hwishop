package com.mallanghwishop.backend.admin.category.application.port.out;

import com.mallanghwishop.backend.admin.category.domain.model.AdminCategory;

public interface SaveCategoryPort {
    Long save(AdminCategory category);
}
