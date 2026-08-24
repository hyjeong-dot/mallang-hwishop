package com.mallanghwishop.backend.popup.adapter.in.web;

import com.mallanghwishop.backend.popup.application.port.in.GetPopupUseCase;
import com.mallanghwishop.backend.popup.application.port.in.ManagePopupUseCase;
import com.mallanghwishop.backend.popup.application.port.in.PopupCommand;
import com.mallanghwishop.backend.popup.domain.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/popups")
@RequiredArgsConstructor
public class AdminPopupController {

    private final ManagePopupUseCase managePopupUseCase;
    private final GetPopupUseCase getPopupUseCase;

    @GetMapping
    public List<Popup> getAllPopups() {
        return getPopupUseCase.getAllPopups();
    }

    @GetMapping("/{id}")
    public Popup getPopup(@PathVariable Long id) {
        return getPopupUseCase.getPopupById(id);
    }

    @PostMapping
    public Popup createPopup(@RequestBody PopupCommand command) {
        return managePopupUseCase.createPopup(command);
    }

    @PutMapping("/{id}")
    public Popup updatePopup(@PathVariable Long id, @RequestBody PopupCommand command) {
        return managePopupUseCase.updatePopup(id, command);
    }

    @DeleteMapping("/{id}")
    public void deletePopup(@PathVariable Long id) {
        managePopupUseCase.deletePopup(id);
    }

    @PatchMapping("/{id}/active")
    public void togglePopupActive(@PathVariable Long id, @RequestParam boolean isActive) {
        managePopupUseCase.togglePopupActive(id, isActive);
    }
}
