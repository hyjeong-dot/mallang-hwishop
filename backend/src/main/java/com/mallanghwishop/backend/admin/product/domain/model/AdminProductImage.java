package com.mallanghwishop.backend.admin.product.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductImage {

    private Long id;
    private Long productId;
    private String srcUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;

    public void updateSortOrder(int newOrder) {
        this.sortOrder = newOrder;
    }
}
