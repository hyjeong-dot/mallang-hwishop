package com.mallanghwishop.backend.product.adapter.out.persistence.entity;

import com.mallanghwishop.backend.product.domain.model.OptionItem;
import jakarta.persistence.*;
import lombok.*;

/**
 * 옵션 항목 JPA 엔티티
 */
@Entity
@Table(name = "option_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "price_delta")
    private Integer priceDelta;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @PrePersist
    protected void onCreate() {
        if (this.priceDelta == null) this.priceDelta = 0;
        if (this.sortOrder == null) this.sortOrder = 0;
    }

    // --- 도메인 변환 ---

    public OptionItem toDomain() {
        return OptionItem.builder()
                .id(id)
                .optionId(optionId)
                .name(name)
                .priceDelta(priceDelta)
                .sortOrder(sortOrder)
                .build();
    }

    public static OptionItemJpaEntity fromDomain(OptionItem item) {
        return OptionItemJpaEntity.builder()
                .id(item.getId())
                .optionId(item.getOptionId())
                .name(item.getName())
                .priceDelta(item.getPriceDelta())
                .sortOrder(item.getSortOrder())
                .build();
    }
}
