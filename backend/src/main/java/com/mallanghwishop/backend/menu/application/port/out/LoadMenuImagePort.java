package com.mallanghwishop.backend.menu.application.port.out;

import com.mallanghwishop.backend.menu.domain.model.MenuImage;
import java.util.List;

/**
 * 메뉴 이미지 로드 포트
 */
public interface LoadMenuImagePort {
    List<MenuImage> findAllByMenuId(Long menuId);
}
