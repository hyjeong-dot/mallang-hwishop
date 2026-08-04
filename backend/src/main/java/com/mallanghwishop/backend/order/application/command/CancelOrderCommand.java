package com.mallanghwishop.backend.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderCommand {
    private Long orderId;
    private String username;
    private String cancelReason;
    private String cancelReasonType;
}
