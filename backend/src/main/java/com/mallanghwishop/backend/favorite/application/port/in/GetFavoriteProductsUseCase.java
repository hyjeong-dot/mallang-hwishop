package com.mallanghwishop.backend.favorite.application.port.in;

import com.mallanghwishop.backend.favorite.application.result.FavoriteProductListResult;
import java.util.UUID;

public interface GetFavoriteProductsUseCase {
    FavoriteProductListResult getFavoriteProducts(UUID memberId);
}
