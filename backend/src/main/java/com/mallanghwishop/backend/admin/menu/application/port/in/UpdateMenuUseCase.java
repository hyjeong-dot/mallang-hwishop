package com.mallanghwishop.backend.admin.menu.application.port.in;

import com.mallanghwishop.backend.admin.menu.application.command.UpdateMenuCommand;

/**
 * 메뉴 수정 유즈케이스 (관리자 전용)
 */
public interface UpdateMenuUseCase {
    void updateMenu(UpdateMenuCommand command);
    void toggleSoldOut(Long id);
}
