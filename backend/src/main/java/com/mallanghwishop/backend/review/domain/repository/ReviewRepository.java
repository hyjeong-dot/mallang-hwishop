package com.mallanghwishop.backend.review.domain.repository;

import com.mallanghwishop.backend.review.adapter.out.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewJpaEntity, Long> {

    List<ReviewJpaEntity> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    Optional<ReviewJpaEntity> findByOrder_Id(Long orderId);

    boolean existsByOrder_Id(Long orderId);

    /** 사용자의 리뷰 수 (스티커 지급 판단용) */
    long countByMemberId(UUID memberId);

    /** 특정 상품가 포함된 주문의 리뷰 조회 (JPA 파생 쿼리) */
    List<ReviewJpaEntity> findByOrder_Items_ProductIdOrderByCreatedAtDesc(Long productId);
}

