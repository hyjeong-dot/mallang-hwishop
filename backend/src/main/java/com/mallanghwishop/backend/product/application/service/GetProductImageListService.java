package com.mallanghwishop.backend.product.application.service;

import com.mallanghwishop.backend.product.application.port.in.GetProductImageListUseCase;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.application.result.ProductImageListResult;
import com.mallanghwishop.backend.product.application.result.ProductImageResult;
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
public class GetProductImageListService implements GetProductImageListUseCase {

    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;

    @Override
    public ProductImageListResult getProductImages(Long productId) {
        Product product = loadProductPort.findAvailableById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<ProductImage> images = productImagePort.findAllByProductId(productId);

        List<ProductImageResult> results = images.stream()
                .map(img -> ProductImageResult.builder()
                        .id(img.getId())
                        .productId(img.getProductId())
                        .srcUrl(img.getSrcUrl())
                        .altText(product.getKorName())
                        .sortOrder(img.getSortOrder() != null ? img.getSortOrder() : 0)
                        .build())
                .collect(Collectors.toList());

        return ProductImageListResult.builder()
                .images(results)
                .build();
    }
}
