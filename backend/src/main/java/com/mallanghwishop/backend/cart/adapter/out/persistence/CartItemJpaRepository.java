package com.mallanghwishop.backend.cart.adapter.out.persistence;

import com.mallanghwishop.backend.cart.domain.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {
}
