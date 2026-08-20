package com.mallanghwishop.backend.admin.order.application.service;

import com.mallanghwishop.backend.admin.order.application.port.in.CreateShippingGroupUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.RefundShippingGroupUseCase;
import com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity;
import com.mallanghwishop.backend.order.adapter.out.persistence.entity.ShippingGroupJpaEntity;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.ShippingGroupJpaRepository;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingGroupService implements CreateShippingGroupUseCase, RefundShippingGroupUseCase {

    private final OrderRepository orderRepository;
    private final ShippingGroupJpaRepository shippingGroupRepository;

    @Override
    @Transactional
    public void createShippingGroup(CreateShippingGroupCommand command) {
        List<OrderJpaEntity> orders = orderRepository.findAllById(command.getOrderIds());
        if (orders.size() < 2) {
            throw new IllegalArgumentException("합배송은 2개 이상의 주문이 필요합니다.");
        }

        OrderJpaEntity firstOrder = orders.get(0);

        for (OrderJpaEntity order : orders) {
            if (!order.getMemberId().equals(firstOrder.getMemberId())) {
                throw new IllegalArgumentException("동일한 회원의 주문만 합배송이 가능합니다.");
            }
            if (!order.getRecipientName().equals(firstOrder.getRecipientName()) ||
                !order.getPhoneNumber().equals(firstOrder.getPhoneNumber()) ||
                !order.getZipcode().equals(firstOrder.getZipcode()) ||
                !order.getAddress().equals(firstOrder.getAddress()) ||
                !order.getDetailAddress().equals(firstOrder.getDetailAddress())) {
                throw new IllegalArgumentException("수령인 정보(이름, 연락처, 주소)가 완전히 동일해야 합배송이 가능합니다.");
            }
            if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.PREPARING) {
                throw new IllegalArgumentException("합배송은 배송 전(PAID, PREPARING) 상태에서만 가능합니다.");
            }
            if (order.getShippingGroupId() != null) {
                throw new IllegalArgumentException("이미 다른 합배송 그룹에 포함된 주문이 있습니다.");
            }
        }

        int maxDeliveryFee = orders.stream().mapToInt(OrderJpaEntity::getDeliveryFee).max().orElse(0);
        int totalDeliveryFee = orders.stream().mapToInt(OrderJpaEntity::getDeliveryFee).sum();
        int refundAmount = totalDeliveryFee - maxDeliveryFee;

        ShippingGroupJpaEntity group = ShippingGroupJpaEntity.builder()
                .memberId(firstOrder.getMemberId())
                .refundAmount(refundAmount)
                .isRefunded(false)
                .build();
        
        group = shippingGroupRepository.save(group);

        for (OrderJpaEntity order : orders) {
            order.setShippingGroupId(group.getId());
            orderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public void refundShippingGroup(Long shippingGroupId) {
        ShippingGroupJpaEntity group = shippingGroupRepository.findById(shippingGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 합배송 그룹입니다."));
        if (group.isRefunded()) {
            throw new IllegalArgumentException("이미 환불 처리된 그룹입니다.");
        }
        group.setRefunded(true);
        shippingGroupRepository.save(group);
    }
}
