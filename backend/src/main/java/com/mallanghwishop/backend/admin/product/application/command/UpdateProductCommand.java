package com.mallanghwishop.backend.admin.product.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductCommand {
    private final Long id;
    private final String korName;
    private final String engName;
    private final String description;
    private final int price;
    private final Integer discountPrice;
    private final Long categoryId;
    private final String imageSrc;
    private final Boolean isAvailable;
    private final Boolean isSoldOut;
    private final Integer sortOrder;
    private final java.time.LocalDateTime saleStartAt;
    private final Integer stock;
    private final Integer maxPerOrder;
}
