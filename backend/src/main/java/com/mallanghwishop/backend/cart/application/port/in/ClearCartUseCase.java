package com.mallanghwishop.backend.cart.application.port.in;

import com.mallanghwishop.backend.cart.application.command.ClearCartCommand;

public interface ClearCartUseCase {
    void clearCart(ClearCartCommand command);
}
