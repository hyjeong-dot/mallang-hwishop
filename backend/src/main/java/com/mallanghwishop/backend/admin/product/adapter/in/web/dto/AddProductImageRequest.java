package com.mallanghwishop.backend.admin.product.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.product.application.command.AddProductImageCommand;
import lombok.Data;

@Data
public class AddProductImageRequest {
    private String srcUrl;
    private Integer sortOrder;

    public AddProductImageCommand toCommand(Long productId) {
        return AddProductImageCommand.builder()
                .productId(productId)
                .srcUrl(srcUrl)
                .sortOrder(sortOrder)
                .build();
    }
}
