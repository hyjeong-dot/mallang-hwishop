package com.mallanghwishop.backend.cart.application.port.out;

import com.mallanghwishop.backend.cart.domain.model.Cart;
import java.util.Optional;
import java.util.UUID;

public interface CartPersistencePort {
    Optional<Cart> findByMemberId(UUID memberId);
    Cart save(Cart cart);
    // orphanRemoval=true 설정이 되어있으므로 CartItem 단품 삭제는 Cart.getItems().remove() 로 처리 가능
}
