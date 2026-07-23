package com.mallanghwishop.backend.admin.cafe.domain.model;

import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeSettings {

    private Long id;
    private String cafeName;
    private String description;
    private String phoneNumber;
    private String address;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean manualClosed; // "is" 접두사 제거 → Lombok이 isManualClosed() 생성
    private String instagramUrl;

    // 비즈니스 로직: 현재 영업 중인지 확인
    public boolean isOpen() {
        if (manualClosed) return false;

        LocalTime now = LocalTime.now();
        if (openTime == null || closeTime == null) return true;

        return !now.isBefore(openTime) && !now.isAfter(closeTime);
    }
}
