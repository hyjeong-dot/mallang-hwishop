package com.mallanghwishop.backend.admin.order.application.service;

import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductJpaRepository;
import com.mallanghwishop.backend.admin.order.application.port.in.GetAdminOrderListUseCase;
import com.mallanghwishop.backend.admin.order.application.port.in.UpdateOrderStatusUseCase;
import com.mallanghwishop.backend.admin.order.application.result.AdminOrderLineItemResult;
import com.mallanghwishop.backend.admin.order.application.result.AdminOrderResult;

import com.mallanghwishop.backend.member.adapter.out.persistence.MemberJpaRepository;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderService implements GetAdminOrderListUseCase, UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;
    private final MemberJpaRepository memberRepository;
    private final ProductJpaRepository productRepository;


    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderResult> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity::toDomain)
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(this::toResult)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .map(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        order.setStatus(status);
        orderRepository.save(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity.fromDomain(order));

        // Phase 2에서 적립금 차감 로직 추가 예정
    }

    private AdminOrderResult toResult(Order order) {
        String nickname = memberRepository.findById(order.getMemberId())
                .map(member -> member.getNickname())
                .orElse("Unknown");

        List<AdminOrderLineItemResult> itemResults = order.getItems().stream()
                .map(item -> {
                    String menuName = productRepository.findById(item.getProductId())
                            .map(p -> p.getKorName())
                            .orElse("삭제된 상품");
                    
                    return AdminOrderLineItemResult.builder()
                            .productId(item.getProductId())
                            .menuName(menuName)
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        return AdminOrderResult.builder()
                .id(order.getId())
                .orderUid(order.getOrderUid())
                .memberId(order.getMemberId())
                .nickname(nickname)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .statusLabel(getStatusLabel(order.getStatus()))
                .orderType(order.getOrderType().name())
                .requestMemo(order.getRequestMemo())
                .items(itemResults)
                .createdAt(order.getCreatedAt())
                .build();
    }

    private String getStatusLabel(OrderStatus status) {
        switch (status) {
            case PENDING: return "결제 대기";
            case PAID: return "결제 완료";
            case PREPARING: return "배송 준비 중";
            case COMPLETED: return "배송 완료";
            case CANCELLED: return "주문 취소";
            default: return status.name();
        }
    }
}
