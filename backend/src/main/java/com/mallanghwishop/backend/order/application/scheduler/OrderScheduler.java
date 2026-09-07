package com.mallanghwishop.backend.order.application.scheduler;

import com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import com.mallanghwishop.backend.point.application.port.in.PointUseCase;
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
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final PointUseCase pointUseCase;
    private final com.mallanghwishop.backend.admin.settings.application.port.in.GetSiteSettingsUseCase getSiteSettingsUseCase;

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    @Transactional
    public void cancelOldPendingOrders() {
        int timeoutMinutes = getSiteSettingsUseCase.getSettings().getCancelTimeoutMinutes();
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(timeoutMinutes);
        List<OrderJpaEntity> oldPendingOrders = orderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.PENDING, thresholdTime);

        for (OrderJpaEntity entity : oldPendingOrders) {
            log.info("Canceling old pending order: {}", entity.getOrderUid());
            
            // 상태 및 사유 업데이트
            entity.setStatus(OrderStatus.CANCELLED);
            entity.setCancelReason("[SYS] 결제 대기 시간(" + timeoutMinutes + "분) 초과로 인한 자동 취소");
            entity.setCancelReasonType("OTHER");
            
            // 포인트 환불 처리
            if (entity.getPointUsed() > 0) {
                pointUseCase.earnPoints(
                        entity.getMemberId(),
                        entity.getPointUsed(),
                        entity.getId(),
                        "주문 자동 취소로 인한 적립금 환불"
                );
            }
        }

        if (!oldPendingOrders.isEmpty()) {
            orderRepository.saveAll(oldPendingOrders);
        }
    }
}
