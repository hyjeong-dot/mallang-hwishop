package com.mallanghwishop.backend.product.application.service;

import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.SaveProductPort;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.product.domain.model.SaleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaleScheduler {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;

    /**
     * 1분마다 실행하여 오픈 예정인 상품들을 체크하고, 시간이 되면 판매 중으로 변경합니다.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateUpcomingSales() {
        List<Product> upcomingProducts = loadProductPort.findAllUpcoming();
        LocalDateTime now = LocalDateTime.now();

        for (Product product : upcomingProducts) {
            if (product.getSaleStartAt() != null && !product.getSaleStartAt().isAfter(now)) {
                log.info("상품 오픈! [ID: {}] {} 상태변경 UPCOMING -> ON_SALE", product.getId(), product.getKorName());
                product.setSaleStatus(SaleStatus.ON_SALE);
                saveProductPort.save(product);
            }
        }
    }
}
