package com.mallanghwishop.backend.product.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 상품 도메인 모델 (순수 POJO)
 * - JPA 어노테이션 없이 비즈니스 로직만 포함
 * - DB 매핑은 ProductJpaEntity에서 담당
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String korName;
    private String engName;
    private String slug;
    private String description;
    private int price;
    private Long categoryId;
    private Boolean isAvailable;
    private Boolean isSoldOut;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- 비즈니스 로직 ---

    public boolean canOrder() {
        return Boolean.TRUE.equals(isAvailable) && !Boolean.TRUE.equals(isSoldOut);
    }

    public void updatePrice(int newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        this.price = newPrice;
    }

    public void markSoldOut() {
        this.isSoldOut = true;
    }

    public void markAvailable() {
        this.isSoldOut = false;
    }

    /**
     * engName → slug 변환 유틸
     * 예: "Purple Latte" → "purple-latte"
     */
    public static String generateSlug(String engName) {
        if (engName == null || engName.isBlank()) return null;
        return engName
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
