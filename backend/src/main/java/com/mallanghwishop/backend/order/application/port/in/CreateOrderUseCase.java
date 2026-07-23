package com.mallanghwishop.backend.order.application.port.in;

import com.mallanghwishop.backend.order.application.port.in.command.CreateOrderCommand;
import com.mallanghwishop.backend.order.application.result.OrderResult;

public interface CreateOrderUseCase {
    OrderResult createOrder(CreateOrderCommand command);
}
