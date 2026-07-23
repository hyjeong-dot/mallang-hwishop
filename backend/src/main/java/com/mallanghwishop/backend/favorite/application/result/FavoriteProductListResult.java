package com.mallanghwishop.backend.favorite.application.result;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class FavoriteProductListResult {
    private final List<FavoriteProductResult> menus;
}
