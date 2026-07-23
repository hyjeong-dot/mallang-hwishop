package com.mallanghwishop.backend.cart.adapter.out.persistence;

import com.mallanghwishop.backend.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberId(UUID memberId);
}
