package com.mallanghwishop.backend.admin.category.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.category.application.command.UpdateCategoryCommand;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private String icon;
    private Integer sortOrder;
    private Boolean isActive;

    public UpdateCategoryCommand toCommand(Long id) {
        return UpdateCategoryCommand.builder()
                .id(id)
                .name(name)
                .icon(icon)
                .sortOrder(sortOrder)
                .isActive(isActive)
                .build();
    }
}
