package com.mallanghwishop.backend.favorite.adapter.in.web.dto;

import com.mallanghwishop.backend.favorite.application.command.ToggleFavoriteCommand;
import lombok.Data;
import java.util.UUID;

@Data
public class ToggleFavoriteRequest {
    private Long productId;

    public ToggleFavoriteCommand toCommand(UUID memberId) {
        return ToggleFavoriteCommand.builder()
                .memberId(memberId)
                .productId(productId)
                .build();
    }
}
