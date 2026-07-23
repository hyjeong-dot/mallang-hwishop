package com.mallanghwishop.backend.cart.application.port.in;

import com.mallanghwishop.backend.cart.application.command.RemoveCartItemCommand;

public interface RemoveCartItemUseCase {
    void removeItem(RemoveCartItemCommand command);
}
