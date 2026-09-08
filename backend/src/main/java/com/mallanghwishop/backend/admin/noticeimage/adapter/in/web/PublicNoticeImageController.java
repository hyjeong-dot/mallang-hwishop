package com.mallanghwishop.backend.admin.noticeimage.adapter.in.web;

import com.mallanghwishop.backend.admin.noticeimage.application.port.in.NoticeImageUseCase;
import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/settings/notices")
@RequiredArgsConstructor
public class PublicNoticeImageController {

    private final NoticeImageUseCase noticeImageUseCase;

    @GetMapping
    public List<NoticeImage> getNoticeImages() {
        return noticeImageUseCase.getNoticeImages();
    }
}
