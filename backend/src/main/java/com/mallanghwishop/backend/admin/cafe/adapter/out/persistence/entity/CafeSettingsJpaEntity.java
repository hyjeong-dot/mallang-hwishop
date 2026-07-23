package com.mallanghwishop.backend.admin.cafe.adapter.out.persistence.entity;

import com.mallanghwishop.backend.admin.cafe.domain.model.CafeSettings;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "cafe_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeSettingsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cafe_name", nullable = false)
    private String cafeName;

    private String description;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "is_manual_closed")
    private boolean manualClosed;

    @Column(name = "instagram_url")
    private String instagramUrl;

    public static CafeSettingsJpaEntity fromDomain(CafeSettings settings) {
        return CafeSettingsJpaEntity.builder()
                .id(settings.getId())
                .cafeName(settings.getCafeName())
                .description(settings.getDescription())
                .phoneNumber(settings.getPhoneNumber())
                .address(settings.getAddress())
                .openTime(settings.getOpenTime())
                .closeTime(settings.getCloseTime())
                .manualClosed(settings.isManualClosed())
                .instagramUrl(settings.getInstagramUrl())
                .build();
    }

    public CafeSettings toDomain() {
        return CafeSettings.builder()
                .id(this.id)
                .cafeName(this.cafeName)
                .description(this.description)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .manualClosed(this.manualClosed)
                .instagramUrl(this.instagramUrl)
                .build();
    }
}
