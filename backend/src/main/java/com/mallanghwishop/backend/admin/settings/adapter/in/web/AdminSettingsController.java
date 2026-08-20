package com.mallanghwishop.backend.admin.settings.adapter.in.web;

import com.mallanghwishop.backend.admin.settings.application.command.UpdateSiteSettingsCommand;
import com.mallanghwishop.backend.admin.settings.application.port.in.GetSiteSettingsUseCase;
import com.mallanghwishop.backend.admin.settings.application.port.in.UpdateSiteSettingsUseCase;
import com.mallanghwishop.backend.admin.settings.application.result.SiteSettingsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/settings")
@RequiredArgsConstructor
public class AdminSettingsController {

    private final GetSiteSettingsUseCase getSiteSettingsUseCase;
    private final UpdateSiteSettingsUseCase updateSiteSettingsUseCase;

    @GetMapping
    public SiteSettingsResult getSettings() {
        return getSiteSettingsUseCase.getSettings();
    }

    @PutMapping
    public SiteSettingsResult updateSettings(@RequestBody UpdateSiteSettingsCommand command) {
        return updateSiteSettingsUseCase.updateSettings(command);
    }
}
