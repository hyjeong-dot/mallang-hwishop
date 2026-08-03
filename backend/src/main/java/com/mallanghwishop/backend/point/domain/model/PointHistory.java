package com.mallanghwishop.backend.point.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointHistory {
    private Long id;
    private UUID memberId;
    private int amount;
    private PointType type;
    private Long orderId;
    private String description;
    private LocalDateTime expireAt;
    private LocalDateTime createdAt;

    public static PointHistory createEarn(UUID memberId, int amount, Long orderId, String description) {
        return PointHistory.builder()
                .memberId(memberId)
                .amount(amount)
                .type(PointType.EARN)
                .orderId(orderId)
                .description(description)
                .expireAt(LocalDateTime.now().plusYears(1))
                .build();
    }

    public static PointHistory createUse(UUID memberId, int amount, Long orderId, String description) {
        return PointHistory.builder()
                .memberId(memberId)
                .amount(-amount)
                .type(PointType.USE)
                .orderId(orderId)
                .description(description)
                .build();
    }
}
