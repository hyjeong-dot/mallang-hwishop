package com.mallanghwishop.backend.menu.adapter.out.persistence;

import com.mallanghwishop.backend.admin.menu.adapter.out.persistence.MenuOptionJpaRepository;
import com.mallanghwishop.backend.admin.menu.adapter.out.persistence.OptionItemJpaRepository;
import com.mallanghwishop.backend.admin.menu.domain.model.MenuOption;
import com.mallanghwishop.backend.admin.menu.domain.model.OptionItem;
import com.mallanghwishop.backend.menu.application.port.out.LoadMenuOptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuOptionPersistenceAdapter implements LoadMenuOptionPort {

    private final MenuOptionJpaRepository menuOptionRepository;
    private final OptionItemJpaRepository optionItemRepository;

    @Override
    public List<MenuOption> findAllByMenuId(Long menuId) {
        return menuOptionRepository.findAllByMenuIdOrderBySortOrderAsc(menuId);
    }

    @Override
    public List<OptionItem> findAllByOptionId(Long optionId) {
        return optionItemRepository.findAllByOptionIdOrderBySortOrderAsc(optionId);
    }
}
