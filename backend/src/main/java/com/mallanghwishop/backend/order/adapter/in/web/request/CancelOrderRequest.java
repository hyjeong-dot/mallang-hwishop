package com.mallanghwishop.backend.order.adapter.in.web.request;

import com.mallanghwishop.backend.order.application.command.CancelOrderCommand;
import lombok.Data;

@Data
public class CancelOrderRequest {
    private String cancelReasonType;
    private String cancelReason;

    public CancelOrderCommand toCommand(Long orderId, String username) {
        return new CancelOrderCommand(orderId, username, cancelReason, cancelReasonType);
    }
}
