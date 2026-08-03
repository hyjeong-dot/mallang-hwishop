package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.command.UpdateProductCommand;
import com.mallanghwishop.backend.admin.product.application.port.in.UpdateProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 수정 전용 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProductService implements UpdateProductUseCase {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;

    @Override
    public void updateProduct(UpdateProductCommand command) {
        if (command.getPrice() < 1000 || command.getPrice() > 50000) {
            throw new IllegalArgumentException("상품 가격은 1,000원 이상 50,000원 이하로 설정해야 합니다.");
        }

        Product product = loadProductPort.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품를 찾을 수 없습니다. ID: " + command.getId()));

        product.setKorName(command.getKorName());
        product.setEngName(command.getEngName());
        product.setDescription(command.getDescription());
        product.setPrice(command.getPrice());
        product.setCategoryId(command.getCategoryId());
        product.setIsAvailable(command.getIsAvailable());
        product.setIsSoldOut(command.getIsSoldOut());
        product.setSortOrder(command.getSortOrder() != null ? command.getSortOrder() : 0);
        product.setSaleStartAt(command.getSaleStartAt());
        product.setStock(command.getStock());
        product.setMaxPerOrder(command.getMaxPerOrder());

        // 예약 판매 상태 갱신 로직
        if (command.getSaleStartAt() != null && command.getSaleStartAt().isAfter(java.time.LocalDateTime.now())) {
            product.setSaleStatus(com.mallanghwishop.backend.product.domain.model.SaleStatus.UPCOMING);
        } else {
            product.setSaleStatus(com.mallanghwishop.backend.product.domain.model.SaleStatus.ON_SALE);
        }

        // slug가 아직 없으면 자동 생성 (이미 있으면 변경하지 않음 — URL 안정성)
        if (product.getSlug() == null && command.getEngName() != null) {
            product.setSlug(generateUniqueSlug(command.getEngName()));
        }

        saveProductPort.save(product);
    }

    /**
     * 유일한 slug 생성 (중복 시 suffix 추가)
     */
    private String generateUniqueSlug(String engName) {
        String baseSlug = Product.generateSlug(engName);
        if (baseSlug == null) return null;

        if (!loadProductPort.existsBySlug(baseSlug)) {
            return baseSlug;
        }

        long count = loadProductPort.countBySlugStartingWith(baseSlug);
        String candidateSlug;
        do {
            count++;
            candidateSlug = baseSlug + "-" + count;
        } while (loadProductPort.existsBySlug(candidateSlug));

        return candidateSlug;
    }

    @Override
    public void toggleSoldOut(Long id) {
        Product product = loadProductPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품를 찾을 수 없습니다. ID: " + id));
        product.setIsSoldOut(!product.getIsSoldOut());
        saveProductPort.save(product);
    }
}
