package com.mallanghwishop.backend.cart.adapter.out.persistence;

import com.mallanghwishop.backend.cart.application.port.out.CartPersistencePort;
import com.mallanghwishop.backend.cart.domain.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.mallanghwishop.backend.cart.adapter.out.persistence.entity.CartJpaEntity;

@Repository
@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartPersistencePort {

    private final CartJpaRepository repository;

    @Override
    public Optional<Cart> findByMemberId(UUID memberId) {
        return repository.findByMemberId(memberId).map(CartJpaEntity::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity entity = CartJpaEntity.fromDomain(cart);
        return repository.save(entity).toDomain();
    }
}
