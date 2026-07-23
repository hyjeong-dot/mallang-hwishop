package com.mallanghwishop.backend.favorite.adapter.out.persistence.entity;

import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member_favorites", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static FavoriteJpaEntity fromDomain(Favorite favorite) {
        return FavoriteJpaEntity.builder()
                .id(favorite.getId())
                .memberId(favorite.getMemberId())
                .productId(favorite.getProductId())
                .createdAt(favorite.getCreatedAt())
                .build();
    }

    public Favorite toDomain() {
        return Favorite.builder()
                .id(this.id)
                .memberId(this.memberId)
                .productId(this.productId)
                .createdAt(this.createdAt)
                .build();
    }
}
