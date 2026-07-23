package com.mallanghwishop.backend.favorite.application.port.out;

import com.mallanghwishop.backend.favorite.domain.model.Favorite;

public interface DeleteFavoritePort {
    /**
     * 찜 정보 삭제
     */
    void delete(Favorite favorite);
}
