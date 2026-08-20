package com.mallanghwishop.backend.admin.delivery.application.command;

import lombok.Data;

@Data
public class UpdateDeliverySettingsCommand {
    private int basicFee;
    private int jejuExtraFee;
}
