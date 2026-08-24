package com.mallanghwishop.backend.popup.application.service;

import com.mallanghwishop.backend.popup.application.port.out.LoadPopupPort;
import com.mallanghwishop.backend.popup.domain.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicPopupService {

    private final LoadPopupPort loadPopupPort;

    @Transactional(readOnly = true)
    public List<Popup> getActivePopups() {
        return loadPopupPort.findAllActive();
    }
}
