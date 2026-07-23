package com.mallanghwishop.backend.admin.order.application.port.in;

import com.mallanghwishop.backend.order.domain.model.OrderStatus;

public interface UpdateOrderStatusUseCase {
    void updateStatus(Long orderId, OrderStatus status);
}
