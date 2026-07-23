package com.mallanghwishop.backend.order.application.port.out;

import com.mallanghwishop.backend.order.domain.model.Order;
import java.util.Optional;
import java.util.List;

public interface OrderPort {
    Order saveOrder(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAllByMemberIdOrderByCreatedAtDesc(java.util.UUID memberId);
}
