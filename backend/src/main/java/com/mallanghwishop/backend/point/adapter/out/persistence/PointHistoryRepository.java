package com.mallanghwishop.backend.point.adapter.out.persistence;

import com.mallanghwishop.backend.point.adapter.out.persistence.entity.PointHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistoryJpaEntity, Long> {
    List<PointHistoryJpaEntity> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PointHistoryJpaEntity p WHERE p.memberId = :memberId AND (p.expireAt IS NULL OR p.expireAt > CURRENT_TIMESTAMP)")
    int calculateValidPointBalance(@Param("memberId") UUID memberId);
}
