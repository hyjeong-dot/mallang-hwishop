package com.mallanghwishop.backend.admin.category.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 관리자용 카테고리 도메인 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategory {

    private Long id;
    private String name;
    private String icon;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- 비즈니스 로직 ---
    public void updateInfo(String name, String icon, Integer sortOrder, Boolean isActive) {
        this.name = name;
        this.icon = icon;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
    }
}
