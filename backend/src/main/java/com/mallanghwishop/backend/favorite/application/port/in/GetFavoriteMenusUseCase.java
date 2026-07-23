package com.mallanghwishop.backend.favorite.application.port.in;

import com.mallanghwishop.backend.favorite.application.result.FavoriteMenuListResult;
import java.util.UUID;

public interface GetFavoriteMenusUseCase {
    FavoriteMenuListResult getFavoriteMenus(UUID memberId);
}
