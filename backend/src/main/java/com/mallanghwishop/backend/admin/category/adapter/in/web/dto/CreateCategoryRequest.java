package com.mallanghwishop.backend.admin.category.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.category.application.command.CreateCategoryCommand;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private String icon;
    private Integer sortOrder;
    private Boolean isActive;

    public CreateCategoryCommand toCommand() {
        return CreateCategoryCommand.builder()
                .name(name)
                .icon(icon)
                .sortOrder(sortOrder)
                .isActive(isActive)
                .build();
    }
}
