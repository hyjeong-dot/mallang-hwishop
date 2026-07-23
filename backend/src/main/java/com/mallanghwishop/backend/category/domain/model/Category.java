package com.mallanghwishop.backend.category.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 사용자용 카테고리 도메인 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private String icon;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
