package com.mallanghwishop.backend.admin.product.application.service;

import com.mallanghwishop.backend.admin.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.admin.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.admin.product.domain.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 서버 시작 시 slug가 없는 기존 상품에 slug를 자동 생성합니다.
 * 한번 실행되면 더 이상 할 일이 없으므로 성능에 영향 없습니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSlugMigration {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;

    @PostConstruct
    @Transactional
    public void migrateNullSlugs() {
        List<Product> productsWithoutSlug = loadProductPort.findAllBySlugIsNull();

        if (productsWithoutSlug.isEmpty()) {
            log.info("[SlugMigration] 모든 상품에 slug가 설정되어 있습니다.");
            return;
        }

        log.info("[SlugMigration] slug가 없는 상품 {}건 발견. 자동 생성 시작...", productsWithoutSlug.size());

        for (Product product : productsWithoutSlug) {
            String baseSlug = Product.generateSlug(product.getEngName());
            if (baseSlug == null) {
                // engName이 없으면 korName 기반으로 id를 사용
                baseSlug = "product-" + product.getId();
            }

            String slug = baseSlug;
            if (loadProductPort.existsBySlug(slug)) {
                long count = loadProductPort.countBySlugStartingWith(baseSlug);
                do {
                    count++;
                    slug = baseSlug + "-" + count;
                } while (loadProductPort.existsBySlug(slug));
            }

            product.setSlug(slug);
            saveProductPort.save(product);
            log.info("[SlugMigration] 상품 '{}' (ID:{}) → slug: '{}'", product.getKorName(), product.getId(), slug);
        }

        log.info("[SlugMigration] slug 마이그레이션 완료.");
    }
}
