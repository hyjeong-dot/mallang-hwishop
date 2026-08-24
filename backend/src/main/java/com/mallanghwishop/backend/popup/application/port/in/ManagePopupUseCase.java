package com.mallanghwishop.backend.popup.application.port.in;

import com.mallanghwishop.backend.popup.domain.model.Popup;

public interface ManagePopupUseCase {
    Popup createPopup(PopupCommand command);
    Popup updatePopup(Long id, PopupCommand command);
    void deletePopup(Long id);
    void togglePopupActive(Long id, boolean isActive);
}
