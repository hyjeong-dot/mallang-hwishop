package com.mallanghwishop.backend.admin.settings.application.service;

import com.mallanghwishop.backend.admin.settings.adapter.out.persistence.SiteSettingsJpaRepository;
import com.mallanghwishop.backend.admin.settings.adapter.out.persistence.entity.SiteSettingsJpaEntity;
import com.mallanghwishop.backend.admin.settings.application.command.UpdateSiteSettingsCommand;
import com.mallanghwishop.backend.admin.settings.application.port.in.GetSiteSettingsUseCase;
import com.mallanghwishop.backend.admin.settings.application.port.in.UpdateSiteSettingsUseCase;
import com.mallanghwishop.backend.admin.settings.application.result.SiteSettingsResult;
import com.mallanghwishop.backend.admin.settings.domain.model.SiteSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteSettingsService implements GetSiteSettingsUseCase, UpdateSiteSettingsUseCase {

    private final SiteSettingsJpaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public SiteSettingsResult getSettings() {
        SiteSettings settings = getOrCreateSettings();
        return toResult(settings);
    }

    @Override
    @Transactional
    public SiteSettingsResult updateSettings(UpdateSiteSettingsCommand command) {
        SiteSettings settings = getOrCreateSettings();

        SiteSettings updated = SiteSettings.builder()
                .basicFee(command.getBasicFee())
                .jejuExtraFee(command.getJejuExtraFee())
                .instagramUrl(command.getInstagramUrl())
                .cancelTimeoutMinutes(command.getCancelTimeoutMinutes())
                .defaultPointRate(command.getDefaultPointRate())
                .minPointUse(command.getMinPointUse())
                .build();

        SiteSettings saved = repository.save(SiteSettingsJpaEntity.fromDomain(updated)).toDomain();
        return toResult(saved);
    }

    private SiteSettings getOrCreateSettings() {
        return repository.findById(1L)
                .map(SiteSettingsJpaEntity::toDomain)
                .orElseGet(() -> repository.save(SiteSettingsJpaEntity.fromDomain(SiteSettings.builder()
                        .basicFee(3500)
                        .jejuExtraFee(3000)
                        .instagramUrl("")
                        .cancelTimeoutMinutes(60)
                        .defaultPointRate(0.03)
                        .minPointUse(1000)
                        .build())).toDomain());
    }

    private SiteSettingsResult toResult(SiteSettings settings) {
        return SiteSettingsResult.builder()
                .basicFee(settings.getBasicFee())
                .jejuExtraFee(settings.getJejuExtraFee())
                .instagramUrl(settings.getInstagramUrl())
                .cancelTimeoutMinutes(settings.getCancelTimeoutMinutes())
                .defaultPointRate(settings.getDefaultPointRate())
                .minPointUse(settings.getMinPointUse())
                .build();
    }
}
