package com.mallanghwishop.backend.favorite.application.port.in;

import com.mallanghwishop.backend.favorite.application.command.ToggleFavoriteCommand;

import com.mallanghwishop.backend.favorite.application.result.ToggleFavoriteResult;

public interface ToggleFavoriteUseCase {
    ToggleFavoriteResult toggleFavorite(ToggleFavoriteCommand command);
}
