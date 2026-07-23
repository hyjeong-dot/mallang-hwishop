package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.port.in.GetProductListUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.admin.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.admin.category.domain.model.AdminCategory;
import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import com.mallanghwishop.backend.admin.product.application.result.ProductListResult;
import com.mallanghwishop.backend.admin.product.application.result.ProductResult;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 목록 조회 전용 서비스
 */
@Service("adminGetProductListService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductListService implements GetProductListUseCase {

    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;
    private final LoadCategoryPort loadCategoryPort;

    @Override
    public ProductListResult getProducts(Long categoryId, String searchQuery) {
        List<Product> products = loadProductPort.findAll(categoryId, searchQuery);
        List<AdminCategory> categories = loadCategoryPort.findAll();

        List<ProductResult> productResults = products.stream()
                .map(product -> {
                    // 카테고리 정보 매핑
                    AdminCategory category = categories.stream()
                            .filter(c -> c.getId().equals(product.getCategoryId()))
                            .findFirst()
                            .orElse(null);

                    // 이미지 경로 매핑 (첫 번째 이미지)
                    List<AdminProductImage> images = productImagePort.findAllByProductId(product.getId());
                    String imageSrc = images.isEmpty() ? "blank.png" : images.get(0).getSrcUrl();

                    return ProductResult.builder()
                        .id(product.getId())
                        .slug(product.getSlug())
                        .korName(product.getKorName())
                        .engName(product.getEngName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .isAvailable(product.getIsAvailable())
                        .isSoldOut(product.getIsSoldOut())
                        .sortOrder(product.getSortOrder())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .categoryName(category != null ? category.getName() : "알 수 없음")
                        .categoryIcon(category != null ? category.getIcon() : "")
                        .imageSrc(imageSrc)
                        .build();
                })
                .collect(Collectors.toList());

        return ProductListResult.builder()
                .products(productResults)
                .productCount(productResults.size())
                .build();
    }
}
