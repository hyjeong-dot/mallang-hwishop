package com.mallanghwishop.backend.favorite.adapter.out.persistence;

import com.mallanghwishop.backend.favorite.adapter.out.persistence.entity.FavoriteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteJpaEntity, Long> {
    Optional<FavoriteJpaEntity> findByMemberIdAndProductId(UUID memberId, Long productId);
    List<FavoriteJpaEntity> findByMemberId(UUID memberId);
}
