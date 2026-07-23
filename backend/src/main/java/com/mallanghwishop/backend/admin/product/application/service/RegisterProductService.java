package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.command.RegisterProductCommand;
import com.mallanghwishop.backend.admin.product.application.port.in.RegisterProductUseCase;
import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 등록 전용 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterProductService implements RegisterProductUseCase {

    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;

    @Override
    public Long registerProduct(RegisterProductCommand command) {
        if (command.getPrice() < 1000 || command.getPrice() > 50000) {
            throw new IllegalArgumentException("상품 가격은 1,000원 이상 50,000원 이하로 설정해야 합니다.");
        }

        String slug = generateUniqueSlug(command.getEngName());

        Product product = Product.builder()
                .korName(command.getKorName())
                .engName(command.getEngName())
                .slug(slug)
                .description(command.getDescription())
                .price(command.getPrice())
                .categoryId(command.getCategoryId())
                .isAvailable(command.getIsAvailable())
                .sortOrder(command.getSortOrder())
                .build();

        return saveProductPort.save(product);
    }

    /**
     * 유일한 slug를 생성합니다.
     * 중복 시 suffix(-2, -3, ...)를 붙입니다.
     */
    private String generateUniqueSlug(String engName) {
        String baseSlug = Product.generateSlug(engName);
        if (baseSlug == null) return null;

        if (!loadProductPort.existsBySlug(baseSlug)) {
            return baseSlug;
        }

        // 중복 발생 시 suffix 추가
        long count = loadProductPort.countBySlugStartingWith(baseSlug);
        String candidateSlug;
        do {
            count++;
            candidateSlug = baseSlug + "-" + count;
        } while (loadProductPort.existsBySlug(candidateSlug));

        return candidateSlug;
    }
}
