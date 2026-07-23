package com.mallanghwishop.backend.admin.cafe.application.port.in;

import com.mallanghwishop.backend.admin.cafe.application.result.CafeSettingsResult;

public interface GetCafeSettingsUseCase {
    CafeSettingsResult getSettings();
}
