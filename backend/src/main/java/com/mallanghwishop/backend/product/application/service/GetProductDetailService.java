package com.mallanghwishop.backend.product.application.service;

import com.mallanghwishop.backend.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.category.domain.model.Category;
import com.mallanghwishop.backend.product.application.port.in.GetProductDetailUseCase;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.application.result.ProductDetailResult;
import com.mallanghwishop.backend.product.domain.exception.ProductNotFoundException;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.product.domain.model.ProductImage;
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
                .isSoldOut(product.getIsSoldOut() || product.getSaleStatus() == com.mallanghwishop.backend.product.domain.model.SaleStatus.SOLD_OUT)
                .isAvailable(product.getIsAvailable())
                .saleStartAt(product.getSaleStartAt())
                .saleStatus(product.getSaleStatus() != null ? product.getSaleStatus().name() : "ON_SALE")
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
