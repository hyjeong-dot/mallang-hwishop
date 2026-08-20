package com.mallanghwishop.backend.admin.delivery.application.port.in;

import com.mallanghwishop.backend.admin.delivery.application.result.DeliverySettingsResult;

public interface GetDeliverySettingsUseCase {
    DeliverySettingsResult getSettings();
}
