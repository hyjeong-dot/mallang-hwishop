package com.mallanghwishop.backend.menu.adapter.out.persistence;

import com.mallanghwishop.backend.menu.domain.model.MenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuImageJpaRepository extends JpaRepository<MenuImage, Long> {
    List<MenuImage> findAllByMenuIdOrderBySortOrderAsc(Long menuId);
}
