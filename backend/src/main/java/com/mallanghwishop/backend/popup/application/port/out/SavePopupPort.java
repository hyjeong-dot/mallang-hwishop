package com.mallanghwishop.backend.popup.application.port.out;

import com.mallanghwishop.backend.popup.domain.model.Popup;

public interface SavePopupPort {
    Popup save(Popup popup);
}
