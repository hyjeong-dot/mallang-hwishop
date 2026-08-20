package com.mallanghwishop.backend.admin.order.application.port.in;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface CreateShippingGroupUseCase {
    void createShippingGroup(CreateShippingGroupCommand command);

    @Getter
    @Builder
    class CreateShippingGroupCommand {
        private final List<Long> orderIds;
    }
}
