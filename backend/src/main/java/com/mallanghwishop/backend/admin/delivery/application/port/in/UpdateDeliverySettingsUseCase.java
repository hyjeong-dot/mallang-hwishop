package com.mallanghwishop.backend.admin.delivery.application.port.in;

import com.mallanghwishop.backend.admin.delivery.application.command.UpdateDeliverySettingsCommand;
import com.mallanghwishop.backend.admin.delivery.application.result.DeliverySettingsResult;

public interface UpdateDeliverySettingsUseCase {
    DeliverySettingsResult updateSettings(UpdateDeliverySettingsCommand command);
}
