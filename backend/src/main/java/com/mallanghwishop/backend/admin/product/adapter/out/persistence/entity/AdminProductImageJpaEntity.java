package com.mallanghwishop.backend.admin.product.adapter.out.persistence.entity;

import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity(name = "AdminProductImage")
@Table(name = "product_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductImageJpaEntity {

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

    public static AdminProductImageJpaEntity fromDomain(AdminProductImage image) {
        return AdminProductImageJpaEntity.builder()
                .id(image.getId())
                .productId(image.getProductId())
                .srcUrl(image.getSrcUrl())
                .sortOrder(image.getSortOrder())
                .createdAt(image.getCreatedAt())
                .build();
    }

    public AdminProductImage toDomain() {
        return AdminProductImage.builder()
                .id(this.id)
                .productId(this.productId)
                .srcUrl(this.srcUrl)
                .sortOrder(this.sortOrder)
                .createdAt(this.createdAt)
                .build();
    }
}
