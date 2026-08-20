package com.mallanghwishop.backend.admin.order.application.port.in;

import com.mallanghwishop.backend.order.domain.model.OrderStatus;

public interface UpdateOrderStatusUseCase {
    void updateStatus(Long orderId, OrderStatus status, String cancelReason, String cancelReasonType);
    void updateTrackingInfo(Long orderId, String trackingCarrier, String trackingNumber);
    void batchUpdateStatus(java.util.List<Long> orderIds, OrderStatus status);
}
