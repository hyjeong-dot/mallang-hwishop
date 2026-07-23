package com.mallanghwishop.backend.order.application.port.in;

import com.mallanghwishop.backend.order.application.result.OrderResult;
import java.util.List;

public interface GetMyOrdersUseCase {
    List<OrderResult> getMyOrders(String username);
}
