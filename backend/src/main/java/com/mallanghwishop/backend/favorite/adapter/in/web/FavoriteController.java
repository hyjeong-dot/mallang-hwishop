package com.mallanghwishop.backend.favorite.adapter.in.web;

import com.mallanghwishop.backend.favorite.application.port.in.CheckFavoriteUseCase;
import com.mallanghwishop.backend.favorite.application.port.in.GetFavoriteMenusUseCase;
import com.mallanghwishop.backend.favorite.application.port.in.ToggleFavoriteUseCase;
import com.mallanghwishop.backend.favorite.application.command.CheckFavoriteCommand;
import com.mallanghwishop.backend.favorite.application.result.CheckFavoriteResult;
import com.mallanghwishop.backend.favorite.application.result.FavoriteMenuListResult;
import com.mallanghwishop.backend.favorite.application.result.ToggleFavoriteResult;
import com.mallanghwishop.backend.favorite.adapter.in.web.dto.ToggleFavoriteRequest;
import com.mallanghwishop.backend.auth.domain.exception.AuthenticationFailedException;
import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final ToggleFavoriteUseCase toggleFavoriteUseCase;
    private final GetFavoriteMenusUseCase getFavoriteMenusUseCase;
    private final CheckFavoriteUseCase checkFavoriteUseCase;
    private final LoadMemberPort loadMemberPort;

    private Optional<UUID> getMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Optional.empty();
        }
        return loadMemberPort.findByUsername(auth.getName())
                .map(member -> member.getId());
    }

    private UUID getMemberIdOrThrow() {
        return getMemberId().orElseThrow(AuthenticationFailedException::new);
    }

    @PostMapping
    public ToggleFavoriteResult toggleFavorite(@RequestBody ToggleFavoriteRequest request) {
        return toggleFavoriteUseCase.toggleFavorite(request.toCommand(getMemberIdOrThrow()));
    }

    @GetMapping
    public FavoriteMenuListResult getMyFavorites() {
        return getFavoriteMenusUseCase.getFavoriteMenus(getMemberIdOrThrow());
    }

    @GetMapping("/{menuId}/check")
    public CheckFavoriteResult checkIfFavorite(@PathVariable Long menuId) {
        return getMemberId()
                .map(memberId -> {
                    CheckFavoriteCommand command = CheckFavoriteCommand.builder()
                            .memberId(memberId)
                            .menuId(menuId)
                            .build();
                    return checkFavoriteUseCase.isFavorite(command);
                })
                .orElseGet(() -> CheckFavoriteResult.builder().isFavorite(false).build());
    }
}
