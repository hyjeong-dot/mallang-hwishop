package com.mallanghwishop.backend.admin.category.application.port.in;

import com.mallanghwishop.backend.admin.category.application.command.UpdateCategoryCommand;

public interface UpdateCategoryUseCase {
    void updateCategory(UpdateCategoryCommand command);
}
