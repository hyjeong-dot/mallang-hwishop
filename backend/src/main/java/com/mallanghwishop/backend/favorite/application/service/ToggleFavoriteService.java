package com.mallanghwishop.backend.favorite.application.service;

import com.mallanghwishop.backend.favorite.application.port.in.ToggleFavoriteUseCase;
import com.mallanghwishop.backend.favorite.application.command.ToggleFavoriteCommand;
import com.mallanghwishop.backend.favorite.application.result.ToggleFavoriteResult;
import com.mallanghwishop.backend.favorite.application.port.out.LoadFavoritePort;
import com.mallanghwishop.backend.favorite.application.port.out.SaveFavoritePort;
import com.mallanghwishop.backend.favorite.application.port.out.DeleteFavoritePort;
import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToggleFavoriteService implements ToggleFavoriteUseCase {

    private final LoadFavoritePort loadFavoritePort;
    private final SaveFavoritePort saveFavoritePort;
    private final DeleteFavoritePort deleteFavoritePort;

    @Override
    @Transactional
    public ToggleFavoriteResult toggleFavorite(ToggleFavoriteCommand command) {
        Optional<Favorite> existing = loadFavoritePort.findByMemberIdAndProductId(command.getMemberId(), command.getProductId());
        
        if (existing.isPresent()) {
            deleteFavoritePort.delete(existing.get());
            return ToggleFavoriteResult.builder().isFavorite(false).build();
        } else {
            Favorite newFavorite = Favorite.builder()
                    .memberId(command.getMemberId())
                    .productId(command.getProductId())
                    .build();
            saveFavoritePort.save(newFavorite);
            return ToggleFavoriteResult.builder().isFavorite(true).build();
        }
    }
}
