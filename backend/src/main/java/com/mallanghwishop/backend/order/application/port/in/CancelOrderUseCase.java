package com.mallanghwishop.backend.order.application.port.in;

import com.mallanghwishop.backend.order.application.command.CancelOrderCommand;

public interface CancelOrderUseCase {
    void cancelOrder(CancelOrderCommand command);
}
