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

@Repository
@RequiredArgsConstructor
public class FavoritePersistenceAdapter implements 
        LoadFavoritePort, 
        LoadFavoriteListPort, 
        SaveFavoritePort, 
        DeleteFavoritePort {

    private final FavoriteJpaRepository repository;

    @Override
    public Optional<Favorite> findByMemberIdAndMenuId(UUID memberId, Long menuId) {
        return repository.findByMemberIdAndMenuId(memberId, menuId);
    }

    @Override
    public Favorite save(Favorite favorite) {
        return repository.save(favorite);
    }

    @Override
    public void delete(Favorite favorite) {
        repository.delete(favorite);
    }

    @Override
    public List<Favorite> findFavoritesByMemberId(UUID memberId) {
        return repository.findByMemberId(memberId);
    }
}
