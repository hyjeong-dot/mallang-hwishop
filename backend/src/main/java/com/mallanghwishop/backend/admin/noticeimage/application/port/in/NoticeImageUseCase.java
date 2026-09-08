package com.mallanghwishop.backend.admin.noticeimage.application.port.in;

import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import java.util.List;

public interface NoticeImageUseCase {
    List<NoticeImage> getNoticeImages();
    NoticeImage addNoticeImage(String url);
    void deleteNoticeImage(Long id);
    void updateNoticeImageSort(List<Long> orderedIds);
}
