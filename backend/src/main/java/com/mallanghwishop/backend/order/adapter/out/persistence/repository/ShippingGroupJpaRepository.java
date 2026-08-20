package com.mallanghwishop.backend.order.adapter.out.persistence.repository;

import com.mallanghwishop.backend.order.adapter.out.persistence.entity.ShippingGroupJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingGroupJpaRepository extends JpaRepository<ShippingGroupJpaEntity, Long> {
}
