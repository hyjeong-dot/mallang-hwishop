package com.mallanghwishop.backend.admin.menu.application.port.in;

import com.mallanghwishop.backend.admin.menu.application.result.MenuResult;

public interface GetMenuUseCase {
    MenuResult getMenu(Long id);
}
