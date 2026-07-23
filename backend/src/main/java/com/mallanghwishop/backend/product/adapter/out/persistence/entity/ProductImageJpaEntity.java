package com.mallanghwishop.backend.product.adapter.out.persistence.entity;

import com.mallanghwishop.backend.product.domain.model.ProductImage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 상품 이미지 JPA 엔티티
 */
@Entity
@Table(name = "product_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "src_url", nullable = false)
    private String srcUrl;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.sortOrder == null) this.sortOrder = 0;
    }

    public void updateSortOrder(int newOrder) {
        this.sortOrder = newOrder;
    }

    // --- 도메인 변환 ---

    public ProductImage toDomain() {
        return ProductImage.builder()
                .id(id)
                .productId(productId)
                .srcUrl(srcUrl)
                .sortOrder(sortOrder)
                .createdAt(createdAt)
                .build();
    }

    public static ProductImageJpaEntity fromDomain(ProductImage image) {
        return ProductImageJpaEntity.builder()
                .id(image.getId())
                .productId(image.getProductId())
                .srcUrl(image.getSrcUrl())
                .sortOrder(image.getSortOrder())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
