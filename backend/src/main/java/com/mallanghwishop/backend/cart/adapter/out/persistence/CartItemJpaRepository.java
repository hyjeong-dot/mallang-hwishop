package com.mallanghwishop.backend.cart.adapter.out.persistence;

import com.mallanghwishop.backend.cart.adapter.out.persistence.entity.CartItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, Long> {
}
