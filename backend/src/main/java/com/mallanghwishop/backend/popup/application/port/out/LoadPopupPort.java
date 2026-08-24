package com.mallanghwishop.backend.popup.application.port.out;

import com.mallanghwishop.backend.popup.domain.model.Popup;
import java.util.List;
import java.util.Optional;

public interface LoadPopupPort {
    List<Popup> findAllActive();
    List<Popup> findAll();
    Optional<Popup> findById(Long id);
}
