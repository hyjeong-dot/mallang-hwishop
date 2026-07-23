package com.mallanghwishop.backend.favorite.application.port.out;

import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import java.util.Optional;
import java.util.UUID;

public interface LoadFavoritePort {
    /**
     * 특정 회원과 상품의 찜 정보 단건 조회
     */
    Optional<Favorite> findByMemberIdAndProductId(UUID memberId, Long productId);
}
