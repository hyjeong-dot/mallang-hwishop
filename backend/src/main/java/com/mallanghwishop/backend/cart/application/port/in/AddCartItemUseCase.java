package com.mallanghwishop.backend.cart.application.port.in;

import com.mallanghwishop.backend.cart.application.command.AddCartItemCommand;

public interface AddCartItemUseCase {
    void addItem(AddCartItemCommand command);
}
