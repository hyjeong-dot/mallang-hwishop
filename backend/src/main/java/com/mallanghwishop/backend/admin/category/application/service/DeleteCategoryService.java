package com.mallanghwishop.backend.admin.category.application.service;

import com.mallanghwishop.backend.admin.category.application.port.in.DeleteCategoryUseCase;
import com.mallanghwishop.backend.admin.category.application.port.out.DeleteCategoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final DeleteCategoryPort deleteCategoryPort;

    @Override
    public void deleteCategory(Long id) {
        deleteCategoryPort.deleteById(id);
    }
}
