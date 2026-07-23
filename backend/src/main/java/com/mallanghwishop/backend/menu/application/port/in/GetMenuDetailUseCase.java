package com.mallanghwishop.backend.menu.application.port.in;

import com.mallanghwishop.backend.menu.application.result.MenuDetailResult;

/**
 * 사용자용 메뉴 상세 조회 유스케이스
 */
public interface GetMenuDetailUseCase {
    MenuDetailResult getAvailableMenu(Long id);
    MenuDetailResult getAvailableMenuBySlug(String slug);
}
