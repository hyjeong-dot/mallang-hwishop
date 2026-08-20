package com.mallanghwishop.backend.admin.delivery.adapter.out.persistence;

import com.mallanghwishop.backend.admin.delivery.adapter.out.persistence.entity.DeliverySettingsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliverySettingsJpaRepository extends JpaRepository<DeliverySettingsJpaEntity, Long> {
}
