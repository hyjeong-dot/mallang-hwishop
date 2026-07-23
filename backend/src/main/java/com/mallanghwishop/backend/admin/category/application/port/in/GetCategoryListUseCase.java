package com.mallanghwishop.backend.admin.category.application.port.in;

import com.mallanghwishop.backend.admin.category.application.result.CategoryListResult;

public interface GetCategoryListUseCase {
    CategoryListResult getCategories();
}
