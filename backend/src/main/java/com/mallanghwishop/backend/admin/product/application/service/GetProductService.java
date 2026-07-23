package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.admin.category.domain.model.AdminCategory;
import com.mallanghwishop.backend.admin.product.application.port.in.GetProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.admin.product.application.result.ProductImageResult;
import com.mallanghwishop.backend.admin.product.application.result.ProductResult;
import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("adminGetProductService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductService implements GetProductUseCase {

    private final LoadProductPort loadProductPort;
    private final LoadCategoryPort loadCategoryPort;
    private final ProductImagePort productImagePort;

    @Override
    public ProductResult getProduct(Long id) {
        Product product = loadProductPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. ID: " + id));

        AdminCategory category = loadCategoryPort.findById(product.getCategoryId())
                .orElse(null);

        // 이미지 조회
        List<AdminProductImage> images = productImagePort.findAllByProductId(product.getId());
        String imageSrc = images.isEmpty() ? "blank.png" : images.get(0).getSrcUrl();
        
        List<ProductImageResult> imageResults = images.stream()
                .map(img -> ProductImageResult.builder()
                        .id(img.getId())
                        .productId(img.getProductId())
                        .srcUrl(img.getSrcUrl())
                        .sortOrder(img.getSortOrder())
                        .build())
                .collect(Collectors.toList());


        return ProductResult.builder()
                .id(product.getId())
                .slug(product.getSlug())
                .korName(product.getKorName())
                .engName(product.getEngName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(category != null ? category.getName() : "알 수 없음")
                .categoryIcon(category != null ? category.getIcon() : "")
                .imageSrc(imageSrc)
                .images(imageResults)
                .isAvailable(product.getIsAvailable())
                .isSoldOut(product.getIsSoldOut())
                .sortOrder(product.getSortOrder())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
