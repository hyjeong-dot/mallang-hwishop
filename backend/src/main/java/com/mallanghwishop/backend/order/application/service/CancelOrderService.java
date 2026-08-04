package com.mallanghwishop.backend.order.application.service;


import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.order.application.port.in.CancelOrderUseCase;
import com.mallanghwishop.backend.order.application.port.out.OrderPort;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import com.mallanghwishop.backend.point.application.port.in.PointUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mallanghwishop.backend.order.application.command.CancelOrderCommand;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderPort orderPort;
    private final LoadMemberPort loadMemberPort;
    private final PointUseCase pointUseCase;


    @Override
    @Transactional
    public void cancelOrder(CancelOrderCommand command) {
        Member member = loadMemberPort.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Order order = orderPort.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getMemberId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 주문만 취소할 수 있습니다.");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("결제 대기 또는 결제 완료 상태에서만 취소할 수 있습니다.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(command.getCancelReason());
        order.setCancelReasonType(command.getCancelReasonType());
        orderPort.saveOrder(order);

        if (order.getPointUsed() > 0) {
            pointUseCase.earnPoints(
                    member.getId(),
                    order.getPointUsed(),
                    order.getId(),
                    "주문 취소로 인한 적립금 환불"
            );
        }
    }
}
