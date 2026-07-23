package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.command.AddProductImageCommand;
import com.mallanghwishop.backend.admin.product.application.port.in.ManageProductImageUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService implements ManageProductImageUseCase {

    private final ProductImagePort productImagePort;

    private final com.mallanghwishop.backend.global.file.application.port.in.DeleteFileUseCase deleteFileUseCase;

    @Override
    public Long addImage(AddProductImageCommand command) {
        AdminProductImage image = AdminProductImage.builder()
                .productId(command.getProductId())
                .srcUrl(command.getSrcUrl())
                .sortOrder(command.getSortOrder())
                .build();
        return productImagePort.saveImage(image);
    }

    @Override
    public void removeImage(Long imageId) {
        // 1. 삭제 전 이미지 정보 조회 (URL 확보)
        productImagePort.findImageById(imageId).ifPresent(image -> {
            // 2. 물리 파일 삭제
            deleteFileUseCase.deleteFile(image.getSrcUrl());
        });
        
        // 3. DB 기록 삭제
        productImagePort.deleteImage(imageId);
        System.out.println("DEBUG: Product image and physical file removed for image ID: " + imageId);
    }

    @Override
    public void updateImageOrder(Long imageId, int newOrder) {
        AdminProductImage image = productImagePort.findImageById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다. ID: " + imageId));
        image.updateSortOrder(newOrder);
        productImagePort.saveImage(image);
    }

    @Override
    public void setPrimaryImage(Long productId, Long imageId) {
        productImagePort.setPrimaryImage(productId, imageId);
    }

    @Override
    public com.mallanghwishop.backend.admin.product.application.result.ProductImageListResult getImagesByProductId(Long productId) {
        java.util.List<AdminProductImage> images = productImagePort.findAllByProductId(productId);
        java.util.List<com.mallanghwishop.backend.admin.product.application.result.ProductImageResult> imageResults = images.stream()
                .map(img -> com.mallanghwishop.backend.admin.product.application.result.ProductImageResult.builder()
                        .id(img.getId())
                        .productId(img.getProductId())
                        .srcUrl(img.getSrcUrl())
                        .sortOrder(img.getSortOrder())
                        .build())
                .collect(java.util.stream.Collectors.toList());
        
        return com.mallanghwishop.backend.admin.product.application.result.ProductImageListResult.builder()
                .images(imageResults)
                .build();
    }
}
