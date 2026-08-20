package com.mallanghwishop.backend.admin.delivery.adapter.out.persistence.entity;

import com.mallanghwishop.backend.admin.delivery.domain.model.DeliverySettings;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_settings")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySettingsJpaEntity {

    @Id
    private Long id; // 단일 레코드 유지를 위해 1 고정

    private int basicFee;
    private int jejuExtraFee;

    public static DeliverySettingsJpaEntity fromDomain(DeliverySettings settings) {
        return DeliverySettingsJpaEntity.builder()
                .id(1L)
                .basicFee(settings.getBasicFee())
                .jejuExtraFee(settings.getJejuExtraFee())
                .build();
    }

    public DeliverySettings toDomain() {
        return DeliverySettings.builder()
                .basicFee(this.basicFee)
                .jejuExtraFee(this.jejuExtraFee)
                .build();
    }
}
