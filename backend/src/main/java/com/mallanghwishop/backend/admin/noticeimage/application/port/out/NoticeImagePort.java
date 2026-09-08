package com.mallanghwishop.backend.admin.noticeimage.application.port.out;

import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import java.util.List;

public interface NoticeImagePort {
    NoticeImage save(NoticeImage noticeImage);
    List<NoticeImage> saveAll(List<NoticeImage> noticeImages);
    List<NoticeImage> findAll();
    NoticeImage findById(Long id);
    void deleteById(Long id);
    Integer getMaxSortOrder();
}
