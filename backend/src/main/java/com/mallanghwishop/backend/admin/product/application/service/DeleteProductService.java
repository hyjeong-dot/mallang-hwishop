package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.port.in.DeleteProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.DeleteProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.admin.product.domain.model.AdminProductImage;
import com.mallanghwishop.backend.global.file.application.port.in.DeleteFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 상품 삭제 전용 서비스
 * - 상품 정보 삭제와 동시에 연관된 물리 이미지 파일도 삭제합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProductService implements DeleteProductUseCase {

    private final DeleteProductPort deleteProductPort;
    private final ProductImagePort productImagePort;
    private final DeleteFileUseCase deleteFileUseCase;

    @Override
    public void deleteProduct(Long id) {
        // 1. 연관된 이미지 정보 조회
        List<AdminProductImage> images = productImagePort.findAllByProductId(id);
        
        // 2. 물리 파일 삭제 (예외가 발생해도 트랜잭션 전체가 롤백되지 않도록 개별 처리 유의 - 현재는 단순 처리)
        for (AdminProductImage image : images) {
            deleteFileUseCase.deleteFile(image.getSrcUrl());
        }
        
        // 3. 이미지 정보(DB) 삭제
        productImagePort.deleteByProductId(id);
        
        // 4. 상품 정보 삭제
        deleteProductPort.deleteById(id);
        
        System.out.println("DEBUG: Product and associated images deleted for ID: " + id);
    }
}
