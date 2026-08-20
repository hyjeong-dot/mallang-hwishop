package com.mallanghwishop.backend.admin.settings.adapter.in.web;

import com.mallanghwishop.backend.admin.settings.application.port.in.GetSiteSettingsUseCase;
import com.mallanghwishop.backend.admin.settings.application.result.SiteSettingsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class PublicSettingsController {

    private final GetSiteSettingsUseCase getSiteSettingsUseCase;

    @GetMapping
    public SiteSettingsResult getSettings() {
        return getSiteSettingsUseCase.getSettings();
    }
}
