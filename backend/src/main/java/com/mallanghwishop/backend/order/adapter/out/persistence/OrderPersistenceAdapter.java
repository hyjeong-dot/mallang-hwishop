package com.mallanghwishop.backend.order.adapter.out.persistence;

import com.mallanghwishop.backend.order.application.port.out.OrderPort;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPort {

    private final OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public java.util.List<Order> findAllByMemberIdOrderByCreatedAtDesc(java.util.UUID memberId) {
        return orderRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
    }
}
