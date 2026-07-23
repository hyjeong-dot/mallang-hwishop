package com.mallanghwishop.backend.product.application.port.out;

import com.mallanghwishop.backend.product.domain.model.ProductOption;
import com.mallanghwishop.backend.product.domain.model.OptionItem;
import java.util.List;

/**
 * 상품 옵션 포트
 */
public interface ProductOptionPort {
    List<ProductOption> findAllByProductId(Long productId);
    List<OptionItem> findAllByOptionId(Long optionId);
    ProductOption saveOption(ProductOption option);
    OptionItem saveItem(OptionItem item);
    void deleteAllByProductId(Long productId);
    void deleteAllItemsByOptionId(Long optionId);
}
