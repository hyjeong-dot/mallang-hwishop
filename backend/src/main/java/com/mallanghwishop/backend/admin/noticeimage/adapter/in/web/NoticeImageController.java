package com.mallanghwishop.backend.admin.noticeimage.adapter.in.web;

import com.mallanghwishop.backend.admin.noticeimage.adapter.in.web.request.AddNoticeImageRequest;
import com.mallanghwishop.backend.admin.noticeimage.adapter.in.web.request.UpdateSortRequest;
import com.mallanghwishop.backend.admin.noticeimage.application.port.in.NoticeImageUseCase;
import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/settings/notices")
@RequiredArgsConstructor
public class NoticeImageController {

    private final NoticeImageUseCase noticeImageUseCase;

    @GetMapping
    public List<NoticeImage> getNoticeImages() {
        return noticeImageUseCase.getNoticeImages();
    }

    @PostMapping
    public NoticeImage addNoticeImage(@RequestBody AddNoticeImageRequest request) {
        return noticeImageUseCase.addNoticeImage(request.getUrl());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoticeImage(@PathVariable Long id) {
        noticeImageUseCase.deleteNoticeImage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/sort")
    public ResponseEntity<Void> updateNoticeImageSort(@RequestBody UpdateSortRequest request) {
        noticeImageUseCase.updateNoticeImageSort(request.getOrderedIds());
        return ResponseEntity.ok().build();
    }
}
