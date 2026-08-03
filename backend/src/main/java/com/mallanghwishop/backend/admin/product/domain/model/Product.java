package com.mallanghwishop.backend.admin.product.domain.model;

import com.mallanghwishop.backend.product.domain.model.SaleStatus;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 관리자 전용 상품 도메인 모델 (순수 POJO)
 * - 관리자 활동(등록, 수정, 삭제)에 필요한 모든 필드와 로직을 포함합니다.
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

    private Integer discountPrice;

    private Long categoryId;

    private Boolean isAvailable;

    private Boolean isSoldOut;

    private Integer sortOrder;

    private java.time.LocalDateTime saleStartAt;
    private SaleStatus saleStatus;
    private Integer stock;
    private Integer maxPerOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void initialize() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isAvailable == null) this.isAvailable = true;
        if (this.isSoldOut == null) this.isSoldOut = false;
        if (this.sortOrder == null) this.sortOrder = 0;
        if (this.slug == null && this.engName != null) {
            this.slug = generateSlug(this.engName);
        }
        // 예약 판매 상태 초기화
        if (this.saleStatus == null) {
            if (this.saleStartAt != null && this.saleStartAt.isAfter(LocalDateTime.now())) {
                this.saleStatus = SaleStatus.UPCOMING;
            } else {
                this.saleStatus = SaleStatus.ON_SALE;
            }
        }
    }

    public void update() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- 관리용 비즈니스 로직 ---
    
    public void updatePrice(int newPrice, Integer discountPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        this.price = newPrice;
        this.discountPrice = discountPrice;
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
