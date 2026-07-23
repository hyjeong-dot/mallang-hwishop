package com.mallanghwishop.backend.product.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 상품 이미지 도메인 모델 (순수 POJO)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    private Long id;
    private Long productId;
    private String srcUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
