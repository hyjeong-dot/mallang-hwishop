package com.mallanghwishop.backend.admin.product.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddProductImageCommand {
    private final Long productId;
    private final String srcUrl;
    private final Integer sortOrder;
}
