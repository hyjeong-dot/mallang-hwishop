package com.mallanghwishop.backend.admin.product.application.result;

import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResult {
    private final Long id;
    private final String slug;
    private final String korName;
    private final String engName;
    private final String description;
    private final int price;
    private final String categoryName;
    private final String categoryIcon;
    private final String imageSrc;
    private final List<ProductImageResult> images;
    private final Boolean isAvailable;
    private final Boolean isSoldOut;
    private final java.time.LocalDateTime saleStartAt;
    private final String saleStatus;
    private final Integer stock;
    private final Integer maxPerOrder;
    private final Integer sortOrder;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
