package com.mallanghwishop.backend.admin.menu.adapter.out.persistence;

import com.mallanghwishop.backend.admin.menu.domain.model.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuOptionJpaRepository extends JpaRepository<MenuOption, Long> {
    List<MenuOption> findAllByMenuIdOrderBySortOrderAsc(Long menuId);
    void deleteByMenuId(Long menuId);
}
