package com.mallanghwishop.backend.favorite.adapter.out.persistence;

import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByMemberIdAndProductId(UUID memberId, Long productId);
    List<Favorite> findByMemberId(UUID memberId);
}
