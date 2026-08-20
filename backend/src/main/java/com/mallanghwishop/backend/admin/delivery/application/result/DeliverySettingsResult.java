package com.mallanghwishop.backend.admin.delivery.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliverySettingsResult {
    private final int basicFee;
    private final int jejuExtraFee;
}
