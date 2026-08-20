package com.mallanghwishop.backend.order.adapter.out.persistence.repository;

import com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, Long> {
    List<OrderJpaEntity> findAllByMemberIdOrderByCreatedAtDesc(UUID memberId);

    java.util.Optional<OrderJpaEntity> findByOrderUid(String orderUid);

    long countByCreatedAtAfter(java.time.LocalDateTime createdAt);

    List<OrderJpaEntity> findAllByCreatedAtAfter(java.time.LocalDateTime createdAt);

    List<OrderJpaEntity> findAllByStatusAndCreatedAtBefore(com.mallanghwishop.backend.order.domain.model.OrderStatus status, java.time.LocalDateTime createdAt);
}
