package com.mallanghwishop.backend.cart.adapter.out.persistence;

import com.mallanghwishop.backend.cart.adapter.out.persistence.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    Optional<CartJpaEntity> findByMemberId(UUID memberId);
}
