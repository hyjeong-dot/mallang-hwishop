package com.mallanghwishop.backend.product.adapter.out.persistence.entity;

import com.mallanghwishop.backend.product.domain.model.Product;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 상품 JPA 엔티티
 * - DB 테이블 매핑은 여기서 담당
 * - 도메인 변환은 toDomain() / fromDomain()
 */
@Entity
@Table(name = "menus")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kor_name", nullable = false)
    private String korName;

    @Column(name = "eng_name")
    private String engName;

    @Column(unique = true)
    private String slug;

    private String description;

    @Column(nullable = false)
    private int price;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_sold_out")
    private Boolean isSoldOut;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isAvailable == null) this.isAvailable = true;
        if (this.isSoldOut == null) this.isSoldOut = false;
        if (this.sortOrder == null) this.sortOrder = 0;
        if (this.slug == null && this.engName != null) {
            this.slug = Product.generateSlug(this.engName);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- 도메인 변환 ---

    public Product toDomain() {
        return Product.builder()
                .id(id)
                .korName(korName)
                .engName(engName)
                .slug(slug)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .isAvailable(isAvailable)
                .isSoldOut(isSoldOut)
                .sortOrder(sortOrder)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static ProductJpaEntity fromDomain(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getId())
                .korName(product.getKorName())
                .engName(product.getEngName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategoryId())
                .isAvailable(product.getIsAvailable())
                .isSoldOut(product.getIsSoldOut())
                .sortOrder(product.getSortOrder())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
