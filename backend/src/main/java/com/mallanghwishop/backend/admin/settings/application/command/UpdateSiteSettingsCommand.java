package com.mallanghwishop.backend.admin.settings.application.command;

import lombok.Data;

@Data
public class UpdateSiteSettingsCommand {
    private int basicFee;
    private int jejuExtraFee;
    private String instagramUrl;
    private int cancelTimeoutMinutes;
}
