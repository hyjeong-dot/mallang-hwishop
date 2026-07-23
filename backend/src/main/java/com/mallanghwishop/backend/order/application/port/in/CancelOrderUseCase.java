package com.mallanghwishop.backend.order.application.port.in;

public interface CancelOrderUseCase {
    void cancelOrder(Long orderId, String username);
}
