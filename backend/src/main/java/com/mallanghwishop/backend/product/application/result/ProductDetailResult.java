package com.mallanghwishop.backend.product.application.result;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResult {
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
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
