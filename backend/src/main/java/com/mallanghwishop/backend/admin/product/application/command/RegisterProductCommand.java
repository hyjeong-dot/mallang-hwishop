package com.mallanghwishop.backend.admin.product.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterProductCommand {
    private final String korName;
    private final String engName;
    private final String description;
    private final int price;
    private final Long categoryId;
    private final Boolean isAvailable;
    private final Integer sortOrder;
}
