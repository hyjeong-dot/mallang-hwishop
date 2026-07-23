package com.mallanghwishop.backend.admin.order.application.port.in;

import com.mallanghwishop.backend.admin.order.application.result.AdminOrderResult;
import java.util.List;

public interface GetAdminOrderListUseCase {
    List<AdminOrderResult> getAllOrders();
}
