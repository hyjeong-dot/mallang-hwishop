package com.mallanghwishop.backend.favorite.application.port.in;

import com.mallanghwishop.backend.favorite.application.command.CheckFavoriteCommand;

import com.mallanghwishop.backend.favorite.application.result.CheckFavoriteResult;

public interface CheckFavoriteUseCase {
    CheckFavoriteResult isFavorite(CheckFavoriteCommand command);
}
