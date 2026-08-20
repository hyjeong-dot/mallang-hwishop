package com.mallanghwishop.backend.product.application.service;

import com.mallanghwishop.backend.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.category.domain.model.Category;
import com.mallanghwishop.backend.product.application.port.in.GetProductListUseCase;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.application.result.ProductListResult;
import com.mallanghwishop.backend.product.application.result.ProductResult;
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
public class GetProductListService implements GetProductListUseCase {

    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;
    private final LoadCategoryPort loadCategoryPort;

    @Override
    public ProductListResult getAvailableProducts(Long categoryId, String searchQuery) {
        List<Product> products;
        if (searchQuery != null && !searchQuery.isBlank()) {
            products = (categoryId != null)
                    ? loadProductPort.searchAvailableByCategoryId(categoryId, searchQuery)
                    : loadProductPort.searchAvailable(searchQuery);
        } else {
            products = (categoryId != null)
                    ? loadProductPort.findAllAvailableByCategoryId(categoryId)
                    : loadProductPort.findAllAvailable();
        }

        List<Category> categories = loadCategoryPort.findAllActive();

        List<ProductResult> results = products.stream().map(product -> {
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

            return ProductResult.builder()
                    .id(product.getId())
                    .slug(product.getSlug())
                    .korName(product.getKorName())
                    .engName(product.getEngName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .discountPrice(product.getDiscountPrice())
                    .categoryName(categoryName)
                    .categoryIcon(categoryIcon)
                    .imageSrc(imageSrc)
                    .isSoldOut(product.getIsSoldOut() || product.getSaleStatus() == com.mallanghwishop.backend.product.domain.model.SaleStatus.SOLD_OUT)
                    .saleStartAt(product.getSaleStartAt())
                    .saleStatus(product.getSaleStatus() != null ? product.getSaleStatus().name() : "ON_SALE")
                    .stock(product.getStock())
                    .maxPerOrder(product.getMaxPerOrder())
                    .instagramUrl(product.getInstagramUrl())
                    .build();
        }).collect(Collectors.toList());

        return ProductListResult.builder()
                .products(results)
                .productCount(results.size())
                .build();
    }
}
