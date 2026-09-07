package com.mallanghwishop.backend.admin.settings.adapter.out.persistence.entity;

import com.mallanghwishop.backend.admin.settings.domain.model.SiteSettings;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_settings")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingsJpaEntity {

    @Id
    private Long id; // 단일 레코드 유지를 위해 1 고정

    private int basicFee;
    private int jejuExtraFee;
    private String instagramUrl;
    private int cancelTimeoutMinutes;

    public static SiteSettingsJpaEntity fromDomain(SiteSettings settings) {
        return SiteSettingsJpaEntity.builder()
                .id(1L)
                .basicFee(settings.getBasicFee())
                .jejuExtraFee(settings.getJejuExtraFee())
                .instagramUrl(settings.getInstagramUrl())
                .cancelTimeoutMinutes(settings.getCancelTimeoutMinutes())
                .build();
    }

    public SiteSettings toDomain() {
        return SiteSettings.builder()
                .basicFee(this.basicFee)
                .jejuExtraFee(this.jejuExtraFee)
                .instagramUrl(this.instagramUrl)
                .cancelTimeoutMinutes(this.cancelTimeoutMinutes)
                .build();
    }
}
