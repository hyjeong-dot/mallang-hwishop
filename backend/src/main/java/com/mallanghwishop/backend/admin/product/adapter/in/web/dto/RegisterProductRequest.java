package com.mallanghwishop.backend.admin.product.adapter.in.web.dto;

import com.mallanghwishop.backend.admin.product.application.command.RegisterProductCommand;
import lombok.Data;

@Data
public class RegisterProductRequest {
    private String korName;
    private String engName;
    private String description;
    private int price;
    private Integer discountPrice;
    private Long categoryId;
    private Boolean isAvailable;
    private Integer sortOrder;
    private java.time.LocalDateTime saleStartAt;
    private Integer stock;
    private Integer maxPerOrder;
    private String instagramUrl;

    public RegisterProductCommand toCommand() {
        return RegisterProductCommand.builder()
                .korName(korName)
                .engName(engName)
                .description(description)
                .price(price)
                .discountPrice(discountPrice)
                .categoryId(categoryId)
                .isAvailable(isAvailable)
                .sortOrder(sortOrder)
                .saleStartAt(saleStartAt)
                .stock(stock)
                .maxPerOrder(maxPerOrder)
                .instagramUrl(instagramUrl)
                .build();
    }
}
