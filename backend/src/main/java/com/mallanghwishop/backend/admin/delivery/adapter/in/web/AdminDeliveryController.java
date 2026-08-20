package com.mallanghwishop.backend.admin.delivery.adapter.in.web;

import com.mallanghwishop.backend.admin.delivery.application.command.UpdateDeliverySettingsCommand;
import com.mallanghwishop.backend.admin.delivery.application.port.in.GetDeliverySettingsUseCase;
import com.mallanghwishop.backend.admin.delivery.application.port.in.UpdateDeliverySettingsUseCase;
import com.mallanghwishop.backend.admin.delivery.application.result.DeliverySettingsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/delivery")
@RequiredArgsConstructor
public class AdminDeliveryController {

    private final GetDeliverySettingsUseCase getDeliverySettingsUseCase;
    private final UpdateDeliverySettingsUseCase updateDeliverySettingsUseCase;

    @GetMapping("/settings")
    public DeliverySettingsResult getSettings() {
        return getDeliverySettingsUseCase.getSettings();
    }

    @PutMapping("/settings")
    public DeliverySettingsResult updateSettings(@RequestBody UpdateDeliverySettingsCommand command) {
        return updateDeliverySettingsUseCase.updateSettings(command);
    }
}
