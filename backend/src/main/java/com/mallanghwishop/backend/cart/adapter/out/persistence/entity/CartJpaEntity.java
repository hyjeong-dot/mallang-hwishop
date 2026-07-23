package com.mallanghwishop.backend.cart.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private UUID memberId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItemJpaEntity> items = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static CartJpaEntity fromDomain(com.mallanghwishop.backend.cart.domain.model.Cart cart) {
        CartJpaEntity entity = CartJpaEntity.builder()
                .id(cart.getId())
                .memberId(cart.getMemberId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
                
        if (cart.getItems() != null) {
            entity.setItems(cart.getItems().stream()
                    .map(item -> CartItemJpaEntity.fromDomain(item, entity))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return entity;
    }

    public com.mallanghwishop.backend.cart.domain.model.Cart toDomain() {
        com.mallanghwishop.backend.cart.domain.model.Cart cart = com.mallanghwishop.backend.cart.domain.model.Cart.builder()
                .id(this.id)
                .memberId(this.memberId)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
                
        if (this.items != null) {
            cart.setItems(this.items.stream()
                    .map(CartItemJpaEntity::toDomain)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return cart;
    }
}
