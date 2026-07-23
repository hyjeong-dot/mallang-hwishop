package com.mallanghwishop.backend.favorite.adapter.out.persistence;

import com.mallanghwishop.backend.favorite.application.port.out.LoadFavoritePort;
import com.mallanghwishop.backend.favorite.application.port.out.LoadFavoriteListPort;
import com.mallanghwishop.backend.favorite.application.port.out.SaveFavoritePort;
import com.mallanghwishop.backend.favorite.application.port.out.DeleteFavoritePort;
import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mallanghwishop.backend.favorite.adapter.out.persistence.entity.FavoriteJpaEntity;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FavoritePersistenceAdapter implements 
        LoadFavoritePort, 
        LoadFavoriteListPort, 
        SaveFavoritePort, 
        DeleteFavoritePort {

    private final FavoriteJpaRepository repository;

    @Override
    public Optional<Favorite> findByMemberIdAndProductId(UUID memberId, Long productId) {
        return repository.findByMemberIdAndProductId(memberId, productId)
                .map(FavoriteJpaEntity::toDomain);
    }

    @Override
    public Favorite save(Favorite favorite) {
        FavoriteJpaEntity entity = FavoriteJpaEntity.fromDomain(favorite);
        return repository.save(entity).toDomain();
    }

    @Override
    public void delete(Favorite favorite) {
        FavoriteJpaEntity entity = FavoriteJpaEntity.fromDomain(favorite);
        repository.delete(entity);
    }

    @Override
    public List<Favorite> findFavoritesByMemberId(UUID memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(FavoriteJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
