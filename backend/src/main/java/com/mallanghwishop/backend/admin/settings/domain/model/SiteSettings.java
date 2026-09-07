package com.mallanghwishop.backend.admin.settings.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SiteSettings {
    private final int basicFee;
    private final int jejuExtraFee;
    private final String instagramUrl;
    private final int cancelTimeoutMinutes;
    private final double defaultPointRate;
    private final int minPointUse;
}
