package com.mallanghwishop.backend.admin.settings.application.port.in;

import com.mallanghwishop.backend.admin.settings.application.command.UpdateSiteSettingsCommand;
import com.mallanghwishop.backend.admin.settings.application.result.SiteSettingsResult;

public interface UpdateSiteSettingsUseCase {
    SiteSettingsResult updateSettings(UpdateSiteSettingsCommand command);
}
