package com.mallanghwishop.backend.popup.application.port.in;

import com.mallanghwishop.backend.popup.domain.model.Popup;
import java.util.List;

public interface GetPopupUseCase {
    List<Popup> getActivePopups();
    List<Popup> getAllPopups();
    Popup getPopupById(Long id);
}
