package com.mallanghwishop.backend.popup.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopupCommand {
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Boolean isActive;
}
