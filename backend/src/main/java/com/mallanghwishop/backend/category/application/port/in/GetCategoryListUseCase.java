package com.mallanghwishop.backend.category.application.port.in;

import com.mallanghwishop.backend.category.application.result.CategoryListResult;

public interface GetCategoryListUseCase {
    CategoryListResult getAllCategories();
}
