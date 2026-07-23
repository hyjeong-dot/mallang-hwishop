package com.mallanghwishop.backend.favorite.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    private Long id;
    private UUID memberId;
    private Long productId;
    private LocalDateTime createdAt;
}
