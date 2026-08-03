package com.mallanghwishop.backend.admin.product.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.product.application.command.UpdateProductCommand;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private String korName;
    private String engName;
    private String description;
    private int price;
    private Long categoryId;
    private Boolean isAvailable;
    private Boolean isSoldOut;
    private Integer sortOrder;
    private java.time.LocalDateTime saleStartAt;

    public UpdateProductCommand toCommand(Long id) {
        return UpdateProductCommand.builder()
                .id(id)
                .korName(korName)
                .engName(engName)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .isAvailable(isAvailable)
                .isSoldOut(isSoldOut)
                .sortOrder(sortOrder)
                .saleStartAt(saleStartAt)
                .build();
    }
}
