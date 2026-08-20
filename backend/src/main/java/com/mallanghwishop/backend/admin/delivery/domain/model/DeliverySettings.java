package com.mallanghwishop.backend.admin.delivery.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliverySettings {
    private final int basicFee;
    private final int jejuExtraFee;
}
