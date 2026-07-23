package com.mallanghwishop.backend.product.adapter.out.persistence.entity;

import com.mallanghwishop.backend.product.domain.model.ProductOption;
import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 옵션 그룹 JPA 엔티티
 */
@Entity
@Table(name = "menu_options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_id", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "is_multi_select")
    private Boolean isMultiSelect;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @PrePersist
    protected void onCreate() {
        if (this.isRequired == null) this.isRequired = false;
        if (this.isMultiSelect == null) this.isMultiSelect = false;
        if (this.sortOrder == null) this.sortOrder = 0;
    }

    // --- 도메인 변환 ---

    public ProductOption toDomain() {
        return ProductOption.builder()
                .id(id)
                .productId(productId)
                .name(name)
                .isRequired(isRequired)
                .isMultiSelect(isMultiSelect)
                .sortOrder(sortOrder)
                .build();
    }

    public static ProductOptionJpaEntity fromDomain(ProductOption option) {
        return ProductOptionJpaEntity.builder()
                .id(option.getId())
                .productId(option.getProductId())
                .name(option.getName())
                .isRequired(option.getIsRequired())
                .isMultiSelect(option.getIsMultiSelect())
                .sortOrder(option.getSortOrder())
                .build();
    }
}
