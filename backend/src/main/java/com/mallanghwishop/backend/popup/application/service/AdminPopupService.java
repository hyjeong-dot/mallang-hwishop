package com.mallanghwishop.backend.popup.application.service;

import com.mallanghwishop.backend.popup.application.port.in.GetPopupUseCase;
import com.mallanghwishop.backend.popup.application.port.in.ManagePopupUseCase;
import com.mallanghwishop.backend.popup.application.port.in.PopupCommand;
import com.mallanghwishop.backend.popup.application.port.out.DeletePopupPort;
import com.mallanghwishop.backend.popup.application.port.out.LoadPopupPort;
import com.mallanghwishop.backend.popup.application.port.out.SavePopupPort;
import com.mallanghwishop.backend.popup.domain.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPopupService implements ManagePopupUseCase, GetPopupUseCase {

    private final LoadPopupPort loadPopupPort;
    private final SavePopupPort savePopupPort;
    private final DeletePopupPort deletePopupPort;

    @Override
    public Popup createPopup(PopupCommand command) {
        Popup popup = Popup.builder()
                .title(command.getTitle())
                .imageUrl(command.getImageUrl())
                .linkUrl(command.getLinkUrl())
                .isActive(command.getIsActive() != null ? command.getIsActive() : false)
                .build();
        return savePopupPort.save(popup);
    }

    @Override
    public Popup updatePopup(Long id, PopupCommand command) {
        Popup popup = getPopupById(id);
        popup.setTitle(command.getTitle());
        popup.setImageUrl(command.getImageUrl());
        popup.setLinkUrl(command.getLinkUrl());
        popup.setIsActive(command.getIsActive() != null ? command.getIsActive() : popup.getIsActive());
        return savePopupPort.save(popup);
    }

    @Override
    public void deletePopup(Long id) {
        deletePopupPort.deleteById(id);
    }

    @Override
    public void togglePopupActive(Long id, boolean isActive) {
        Popup popup = getPopupById(id);
        popup.setIsActive(isActive);
        savePopupPort.save(popup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Popup> getActivePopups() {
        return loadPopupPort.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Popup> getAllPopups() {
        return loadPopupPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Popup getPopupById(Long id) {
        return loadPopupPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("팝업을 찾을 수 없습니다. ID: " + id));
    }
}
