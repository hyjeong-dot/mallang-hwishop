package com.mallanghwishop.backend.favorite.application.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ToggleFavoriteResult {
    @JsonProperty("isFavorite")
    private final boolean isFavorite;
}
