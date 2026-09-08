package com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence;

import com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence.entity.NoticeImageJpaEntity;
import com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence.repository.NoticeImageRepository;
import com.mallanghwishop.backend.admin.noticeimage.application.port.out.NoticeImagePort;
import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoticeImagePersistenceAdapter implements NoticeImagePort {

    private final NoticeImageRepository noticeImageRepository;

    @Override
    public NoticeImage save(NoticeImage noticeImage) {
        NoticeImageJpaEntity entity = NoticeImageJpaEntity.fromDomain(noticeImage);
        return noticeImageRepository.save(entity).toDomain();
    }

    @Override
    public List<NoticeImage> saveAll(List<NoticeImage> noticeImages) {
        List<NoticeImageJpaEntity> entities = noticeImages.stream()
                .map(NoticeImageJpaEntity::fromDomain)
                .collect(Collectors.toList());
        return noticeImageRepository.saveAll(entities).stream()
                .map(NoticeImageJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeImage> findAll() {
        return noticeImageRepository.findAllByOrderBySortOrderAsc().stream()
                .map(NoticeImageJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeImage findById(Long id) {
        return noticeImageRepository.findById(id)
                .map(NoticeImageJpaEntity::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        noticeImageRepository.deleteById(id);
    }

    @Override
    public Integer getMaxSortOrder() {
        Integer max = noticeImageRepository.findMaxSortOrder();
        return max != null ? max : 0;
    }
}
