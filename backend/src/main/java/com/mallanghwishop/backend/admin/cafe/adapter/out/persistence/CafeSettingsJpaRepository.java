package com.mallanghwishop.backend.admin.cafe.adapter.out.persistence;

import com.mallanghwishop.backend.admin.cafe.adapter.out.persistence.entity.CafeSettingsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeSettingsJpaRepository extends JpaRepository<CafeSettingsJpaEntity, Long> {
}
