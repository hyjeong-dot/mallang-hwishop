package com.mallanghwishop.backend.product.adapter.in.web;

import com.mallanghwishop.backend.product.application.port.in.GetProductDetailUseCase;
import com.mallanghwishop.backend.product.application.port.in.GetProductImageListUseCase;
import com.mallanghwishop.backend.product.application.port.in.GetProductListUseCase;
import com.mallanghwishop.backend.product.application.result.ProductDetailResult;
import com.mallanghwishop.backend.product.application.result.ProductImageListResult;
import com.mallanghwishop.backend.product.application.result.ProductListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자용 상품 조회 컨트롤러
 * - /menus 경로 유지 (프론트엔드 호환)
 */
@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductListUseCase getProductListUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final GetProductImageListUseCase getProductImageListUseCase;

    @GetMapping
    public ProductListResult getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String searchQuery) {
        return getProductListUseCase.getAvailableProducts(categoryId, searchQuery);
    }

    @GetMapping("/{id:\\d+}")
    public ProductDetailResult getProduct(@PathVariable Long id) {
        return getProductDetailUseCase.getAvailableProduct(id);
    }

    @GetMapping("/{slug:[a-z][a-z0-9-]*}")
    public ProductDetailResult getProductBySlug(@PathVariable String slug) {
        return getProductDetailUseCase.getAvailableProductBySlug(slug);
    }

    @GetMapping("/{id:\\d+}/images")
    public ProductImageListResult getProductImages(@PathVariable Long id) {
        return getProductImageListUseCase.getProductImages(id);
    }
}
