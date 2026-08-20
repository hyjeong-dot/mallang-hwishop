package com.mallanghwishop.backend.admin.delivery.application.service;

import com.mallanghwishop.backend.admin.delivery.adapter.out.persistence.DeliverySettingsJpaRepository;
import com.mallanghwishop.backend.admin.delivery.adapter.out.persistence.entity.DeliverySettingsJpaEntity;
import com.mallanghwishop.backend.admin.delivery.application.command.UpdateDeliverySettingsCommand;
import com.mallanghwishop.backend.admin.delivery.application.port.in.GetDeliverySettingsUseCase;
import com.mallanghwishop.backend.admin.delivery.application.port.in.UpdateDeliverySettingsUseCase;
import com.mallanghwishop.backend.admin.delivery.application.result.DeliverySettingsResult;
import com.mallanghwishop.backend.admin.delivery.domain.model.DeliverySettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliverySettingsService implements GetDeliverySettingsUseCase, UpdateDeliverySettingsUseCase {

    private final DeliverySettingsJpaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public DeliverySettingsResult getSettings() {
        DeliverySettings settings = getOrCreateSettings();
        return toResult(settings);
    }

    @Override
    @Transactional
    public DeliverySettingsResult updateSettings(UpdateDeliverySettingsCommand command) {
        DeliverySettings settings = getOrCreateSettings();

        DeliverySettings updated = DeliverySettings.builder()
                .basicFee(command.getBasicFee())
                .jejuExtraFee(command.getJejuExtraFee())
                .build();

        DeliverySettings saved = repository.save(DeliverySettingsJpaEntity.fromDomain(updated)).toDomain();
        return toResult(saved);
    }

    private DeliverySettings getOrCreateSettings() {
        return repository.findById(1L)
                .map(DeliverySettingsJpaEntity::toDomain)
                .orElseGet(() -> repository.save(DeliverySettingsJpaEntity.fromDomain(DeliverySettings.builder()
                        .basicFee(3500)
                        .jejuExtraFee(3000)
                        .build())).toDomain());
    }

    private DeliverySettingsResult toResult(DeliverySettings settings) {
        return DeliverySettingsResult.builder()
                .basicFee(settings.getBasicFee())
                .jejuExtraFee(settings.getJejuExtraFee())
                .build();
    }
}
