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
    private Integer discountPrice;
    private Long categoryId;
    private Boolean isAvailable;
    private Boolean isSoldOut;
    private Integer sortOrder;
    private LocalDateTime saleStartAt;
    private SaleStatus saleStatus;
    private Integer stock;
    private Integer maxPerOrder;
    private String instagramUrl;
    private Double pointRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- 비즈니스 로직 ---

    public boolean canOrder() {
        if (!Boolean.TRUE.equals(isAvailable)) return false;
        if (saleStatus == SaleStatus.SOLD_OUT || Boolean.TRUE.equals(isSoldOut)) return false;
        if (saleStatus == SaleStatus.UPCOMING) {
            // 스케줄러가 아직 상태를 ON_SALE로 바꾸지 않았더라도, 지정 시간이 지났다면 구매 허용
            if (saleStartAt != null && !saleStartAt.isAfter(LocalDateTime.now())) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void updatePrice(int newPrice, Integer discountPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        this.price = newPrice;
        this.discountPrice = discountPrice;
    }

    public void markSoldOut() {
        this.isSoldOut = true;
    }

    public void decreaseStock(int quantity) {
        if (this.stock == null) {
            // 무제한 재고
            return;
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다. 남은 수량: " + this.stock);
        }
        this.stock -= quantity;
        if (this.stock == 0) {
            this.saleStatus = SaleStatus.SOLD_OUT;
            this.isSoldOut = true;
        }
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
