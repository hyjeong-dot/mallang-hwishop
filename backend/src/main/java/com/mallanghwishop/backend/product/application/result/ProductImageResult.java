package com.mallanghwishop.backend.product.application.result;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResult {
    private Long id;
    private Long productId;
    private String srcUrl;
    private String altText;
    private int sortOrder;
}
