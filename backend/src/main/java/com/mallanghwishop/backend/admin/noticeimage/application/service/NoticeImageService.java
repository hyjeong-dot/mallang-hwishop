package com.mallanghwishop.backend.admin.noticeimage.application.service;

import com.mallanghwishop.backend.admin.noticeimage.application.port.in.NoticeImageUseCase;
import com.mallanghwishop.backend.admin.noticeimage.application.port.out.NoticeImagePort;
import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImageService implements NoticeImageUseCase {

    private final NoticeImagePort noticeImagePort;

    @Override
    @Transactional(readOnly = true)
    public List<NoticeImage> getNoticeImages() {
        return noticeImagePort.findAll();
    }

    @Override
    public NoticeImage addNoticeImage(String url) {
        Integer maxSortOrder = noticeImagePort.getMaxSortOrder();
        NoticeImage newImage = NoticeImage.builder()
                .url(url)
                .sortOrder(maxSortOrder + 1)
                .build();
        return noticeImagePort.save(newImage);
    }

    @Override
    public void deleteNoticeImage(Long id) {
        noticeImagePort.deleteById(id);
    }

    @Override
    public void updateNoticeImageSort(List<Long> orderedIds) {
        List<NoticeImage> currentImages = noticeImagePort.findAll();
        Map<Long, NoticeImage> imageMap = currentImages.stream()
                .collect(Collectors.toMap(NoticeImage::getId, Function.identity()));

        List<NoticeImage> updatedImages = new ArrayList<>();
        int order = 1;
        for (Long id : orderedIds) {
            NoticeImage image = imageMap.get(id);
            if (image != null) {
                image.setSortOrder(order++);
                updatedImages.add(image);
            }
        }
        
        noticeImagePort.saveAll(updatedImages);
    }
}
