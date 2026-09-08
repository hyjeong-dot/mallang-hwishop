package com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence.repository;

import com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence.entity.NoticeImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeImageRepository extends JpaRepository<NoticeImageJpaEntity, Long> {
    List<NoticeImageJpaEntity> findAllByOrderBySortOrderAsc();

    @Query("SELECT MAX(n.sortOrder) FROM NoticeImageJpaEntity n")
    Integer findMaxSortOrder();
}
