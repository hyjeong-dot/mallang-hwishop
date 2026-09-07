package com.mallanghwishop.backend.admin.settings.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SiteSettingsResult {
    private final int basicFee;
    private final int jejuExtraFee;
    private final String instagramUrl;
    private final int cancelTimeoutMinutes;
}
