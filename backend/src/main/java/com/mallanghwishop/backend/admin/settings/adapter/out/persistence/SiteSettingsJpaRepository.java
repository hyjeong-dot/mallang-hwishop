package com.mallanghwishop.backend.admin.settings.adapter.out.persistence;

import com.mallanghwishop.backend.admin.settings.adapter.out.persistence.entity.SiteSettingsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingsJpaRepository extends JpaRepository<SiteSettingsJpaEntity, Long> {
}
