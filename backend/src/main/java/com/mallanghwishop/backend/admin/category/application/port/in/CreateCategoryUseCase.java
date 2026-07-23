package com.mallanghwishop.backend.admin.category.application.port.in;

import com.mallanghwishop.backend.admin.category.application.command.CreateCategoryCommand;

public interface CreateCategoryUseCase {
    Long createCategory(CreateCategoryCommand command);
}
