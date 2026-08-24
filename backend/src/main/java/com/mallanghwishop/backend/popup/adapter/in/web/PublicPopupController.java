package com.mallanghwishop.backend.popup.adapter.in.web;

import com.mallanghwishop.backend.popup.application.service.PublicPopupService;
import com.mallanghwishop.backend.popup.domain.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/popups")
@RequiredArgsConstructor
public class PublicPopupController {

    private final PublicPopupService publicPopupService;

    @GetMapping("/active")
    public List<Popup> getActivePopups() {
        return publicPopupService.getActivePopups();
    }
}
