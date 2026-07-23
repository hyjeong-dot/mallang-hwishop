package com.mallanghwishop.backend.favorite.application.port.out;

import com.mallanghwishop.backend.favorite.domain.model.Favorite;

public interface SaveFavoritePort {
    /**
     * 찜 정보 쓰기 (생성 또는 저장)
     */
    Favorite save(Favorite favorite);
}
