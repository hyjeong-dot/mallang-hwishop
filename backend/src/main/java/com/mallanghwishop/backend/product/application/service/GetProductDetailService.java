package com.mallanghwishop.backend.product.application.service;

import com.mallanghwishop.backend.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.category.domain.model.Category;
import com.mallanghwishop.backend.product.application.port.in.GetProductDetailUseCase;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.application.port.out.ProductOptionPort;
import com.mallanghwishop.backend.product.application.result.ProductDetailResult;
import com.mallanghwishop.backend.product.application.result.ProductOptionResult;
import com.mallanghwishop.backend.product.application.result.OptionItemResult;
import com.mallanghwishop.backend.product.domain.exception.ProductNotFoundException;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.product.domain.model.ProductImage;
import com.mallanghwishop.backend.product.domain.model.ProductOption;
import com.mallanghwishop.backend.product.domain.model.OptionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductDetailService implements GetProductDetailUseCase {

    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;
    private final LoadCategoryPort loadCategoryPort;
    private final ProductOptionPort productOptionPort;

    @Override
    public ProductDetailResult getAvailableProduct(Long id) {
        Product product = loadProductPort.findAvailableById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return buildProductDetailResult(product);
    }

    @Override
    public ProductDetailResult getAvailableProductBySlug(String slug) {
        Product product = loadProductPort.findAvailableBySlug(slug)
                .orElseThrow(() -> new ProductNotFoundException(slug));
        return buildProductDetailResult(product);
    }

    private ProductDetailResult buildProductDetailResult(Product product) {
        List<Category> categories = loadCategoryPort.findAllActive();
        String categoryName = "";
        String categoryIcon = "";
        for (Category cat : categories) {
            if (cat.getId().equals(product.getCategoryId())) {
                categoryName = cat.getName();
                categoryIcon = cat.getIcon();
                break;
            }
        }

        List<ProductImage> images = productImagePort.findAllByProductId(product.getId());
        String imageSrc = images.isEmpty() ? "blank.png" : images.get(0).getSrcUrl();

        List<ProductOption> options = productOptionPort.findAllByProductId(product.getId());
        List<ProductOptionResult> optionResults = options.stream()
                .map(opt -> {
                    List<OptionItem> items = productOptionPort.findAllByOptionId(opt.getId());
                    List<OptionItemResult> itemResults = items.stream()
                            .map(item -> OptionItemResult.builder()
                                    .id(item.getId())
                                    .optionId(item.getOptionId())
                                    .name(item.getName())
                                    .priceDelta(item.getPriceDelta())
                                    .sortOrder(item.getSortOrder())
                                    .build())
                            .collect(Collectors.toList());

                    return ProductOptionResult.builder()
                            .id(opt.getId())
                            .productId(opt.getProductId())
                            .name(opt.getName())
                            .isRequired(opt.getIsRequired())
                            .isMultiSelect(opt.getIsMultiSelect())
                            .sortOrder(opt.getSortOrder())
                            .items(itemResults)
                            .build();
                })
                .collect(Collectors.toList());

        return ProductDetailResult.builder()
                .id(product.getId())
                .slug(product.getSlug())
                .korName(product.getKorName())
                .engName(product.getEngName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(categoryName)
                .categoryIcon(categoryIcon)
                .imageSrc(imageSrc)
                .isSoldOut(product.getIsSoldOut())
                .isAvailable(product.getIsAvailable())
                .options(optionResults)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
