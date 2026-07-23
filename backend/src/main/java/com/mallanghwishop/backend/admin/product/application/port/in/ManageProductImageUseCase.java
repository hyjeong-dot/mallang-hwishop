package com.mallanghwishop.backend.admin.product.application.port.in;

import com.mallanghwishop.backend.admin.product.application.command.AddProductImageCommand;
import com.mallanghwishop.backend.admin.product.application.result.ProductImageListResult;

public interface ManageProductImageUseCase {
    Long addImage(AddProductImageCommand command);
    void removeImage(Long imageId);
    void updateImageOrder(Long imageId, int newOrder);
    void setPrimaryImage(Long productId, Long imageId);
    ProductImageListResult getImagesByProductId(Long productId);
}
