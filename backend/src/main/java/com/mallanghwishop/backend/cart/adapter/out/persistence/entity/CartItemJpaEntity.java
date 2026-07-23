package com.mallanghwishop.backend.cart.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @ToString.Exclude
    private CartJpaEntity cart;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "selected_option_names", length = 1000)
    private String selectedOptionNames;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static CartItemJpaEntity fromDomain(com.mallanghwishop.backend.cart.domain.model.CartItem item, CartJpaEntity cartEntity) {
        return CartItemJpaEntity.builder()
                .id(item.getId())
                .cart(cartEntity)
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .selectedOptionNames(item.getSelectedOptionNames())
                .createdAt(item.getCreatedAt())
                .build();
    }

    public com.mallanghwishop.backend.cart.domain.model.CartItem toDomain() {
        return com.mallanghwishop.backend.cart.domain.model.CartItem.builder()
                .id(this.id)
                .productId(this.productId)
                .quantity(this.quantity)
                .unitPrice(this.unitPrice)
                .selectedOptionNames(this.selectedOptionNames)
                .createdAt(this.createdAt)
                .build();
    }
}
