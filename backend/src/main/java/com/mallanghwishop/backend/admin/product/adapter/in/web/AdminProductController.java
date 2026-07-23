package com.mallanghwishop.backend.admin.product.adapter.in.web;

import com.mallanghwishop.backend.admin.product.adapter.in.web.dto.RegisterProductRequest;
import com.mallanghwishop.backend.admin.product.adapter.in.web.dto.UpdateProductRequest;

import com.mallanghwishop.backend.admin.product.application.port.in.DeleteProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.in.GetProductListUseCase;
import com.mallanghwishop.backend.admin.product.application.port.in.GetProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.in.RegisterProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.in.UpdateProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.in.ManageProductImageUseCase;
import com.mallanghwishop.backend.admin.product.adapter.in.web.dto.AddProductImageRequest;
import com.mallanghwishop.backend.admin.product.application.result.ProductImageListResult;
import com.mallanghwishop.backend.admin.product.application.result.ProductResult;
import com.mallanghwishop.backend.admin.product.application.result.ProductListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final GetProductListUseCase getProductListUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ManageProductImageUseCase manageProductImageUseCase;

    @GetMapping
    public ProductListResult getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String searchQuery) {
        return getProductListUseCase.getProducts(categoryId, searchQuery);
    }

    @GetMapping("/{id}")
    public ProductResult getProduct(@PathVariable Long id) {
        return getProductUseCase.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerProduct(@RequestBody RegisterProductRequest request) {
        return registerProductUseCase.registerProduct(request.toCommand());
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        updateProductUseCase.updateProduct(request.toCommand(id));
    }

    @PatchMapping("/{id}/sold-out")
    public void toggleSoldOut(@PathVariable Long id) {
        updateProductUseCase.toggleSoldOut(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.deleteProduct(id);
    }

    // --- 상품 이미지 관리 ---
    
    @GetMapping("/{id}/product-images")
    public ProductImageListResult getProductImages(@PathVariable Long id) {
        return manageProductImageUseCase.getImagesByProductId(id);
    }

    @PostMapping("/{id}/product-images")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addProductImage(@PathVariable Long id, @RequestBody AddProductImageRequest request) {
        return manageProductImageUseCase.addImage(request.toCommand(id));
    }

    @PatchMapping("/product-images/{imageId}/order")
    public void updateImageOrder(@PathVariable Long imageId, @RequestParam int newOrder) {
        manageProductImageUseCase.updateImageOrder(imageId, newOrder);
    }

    @PatchMapping("/{id}/product-images/{imageId}/primary")
    public void setPrimaryImage(@PathVariable Long id, @PathVariable Long imageId) {
        manageProductImageUseCase.setPrimaryImage(id, imageId);
    }

    @DeleteMapping("/product-images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductImage(@PathVariable Long imageId) {
        manageProductImageUseCase.removeImage(imageId);
    }

}
