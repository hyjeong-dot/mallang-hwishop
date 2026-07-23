package com.mallanghwishop.backend.admin.product.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.product.application.command.RegisterProductCommand;
import lombok.Data;

@Data
public class RegisterProductRequest {
    private String korName;
    private String engName;
    private String description;
    private int price;
    private Long categoryId;
    private Boolean isAvailable;
    private Integer sortOrder;

    public RegisterProductCommand toCommand() {
        return RegisterProductCommand.builder()
                .korName(korName)
                .engName(engName)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .isAvailable(isAvailable)
                .sortOrder(sortOrder)
                .build();
    }
}
