package com.mallanghwishop.backend.cart.application.port.in;

import com.mallanghwishop.backend.cart.application.command.UpdateCartItemCommand;

public interface UpdateCartItemUseCase {
    void updateQuantity(UpdateCartItemCommand command);
}
