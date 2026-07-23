package com.mallanghwishop.backend.product.application.result;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResult {
    private Long id;
    private String slug;
    private String korName;
    private String engName;
    private String description;
    private int price;
    private String categoryName;
    private String categoryIcon;
    private String imageSrc;
    private Boolean isSoldOut;
}
