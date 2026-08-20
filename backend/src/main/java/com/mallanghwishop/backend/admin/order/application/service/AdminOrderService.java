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
import com.mallanghwishop.backend.point.application.port.in.PointUseCase;
import com.mallanghwishop.backend.payment.application.service.PaymentService;
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
    private final PointUseCase pointUseCase;
    private final PaymentService paymentService;

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
    public void updateStatus(Long orderId, OrderStatus status, String cancelReason, String cancelReasonType) {
        Order order = orderRepository.findById(orderId)
                .map(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        OrderStatus previousStatus = order.getStatus();
        order.setStatus(status);
        
        if (status == OrderStatus.CANCELLED) {
            order.setCancelReason(cancelReason);
            order.setCancelReasonType(cancelReasonType);

            // 주문 취소 시 사용한 적립금 환불
            if (order.getPointUsed() > 0 && previousStatus != OrderStatus.CANCELLED) {
                pointUseCase.earnPoints(
                        order.getMemberId(),
                        order.getPointUsed(),
                        order.getId(),
                        "주문 취소로 인한 적립금 환불"
                );
            }

            // 주문 취소 시 지급된 적립금 회수 (이전 상태가 COMPLETED인 경우)
            if (previousStatus == OrderStatus.COMPLETED && order.getPointEarned() > 0) {
                pointUseCase.usePoints(
                        order.getMemberId(),
                        order.getPointEarned(),
                        order.getId(),
                        "주문 취소로 인한 적립금 회수"
                );
            }
            
            // Toss 결제 취소 요청
            if (order.getPaymentKey() != null) {
                paymentService.cancelPayment(order.getPaymentKey(), cancelReason);
            }
        }
        
        orderRepository.save(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity.fromDomain(order));

        // 배송 완료로 상태가 변경되는 경우, 그리고 이전 상태가 완료가 아니었던 경우 적립금 지급
        if (status == OrderStatus.COMPLETED && previousStatus != OrderStatus.COMPLETED) {
            if (order.getPointEarned() > 0) {
                pointUseCase.earnPoints(
                        order.getMemberId(),
                        order.getPointEarned(),
                        order.getId(),
                        "주문 #" + order.getOrderUid() + " 구매 적립금"
                );
            }
        }
    }

    @Override
    public void updateTrackingInfo(Long orderId, String trackingCarrier, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
                .map(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        order.setTrackingCarrier(trackingCarrier);
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(com.mallanghwishop.backend.order.adapter.out.persistence.entity.OrderJpaEntity.fromDomain(order));
    }

    @Override
    public void batchUpdateStatus(List<Long> orderIds, OrderStatus status) {
        for (Long id : orderIds) {
            updateStatus(id, status, null, null);
        }
    }

    private AdminOrderResult toResult(Order order) {
        String name = memberRepository.findById(order.getMemberId())
                .map(member -> member.getName())
                .orElse("Unknown");

        List<AdminOrderLineItemResult> itemResults = order.getItems().stream()
                .map(item -> {
                    String menuName = productRepository.findById(item.getProductId())
                            .map(p -> p.getKorName())
                            .orElse("삭제된 상품");
                    
                    return AdminOrderLineItemResult.builder()
                            .productId(item.getProductId())
                            .productName(menuName)
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        return AdminOrderResult.builder()
                .id(order.getId())
                .orderUid(order.getOrderUid())
                .memberId(order.getMemberId())
                .name(name)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .statusLabel(getStatusLabel(order.getStatus()))
                .recipientName(order.getRecipientName())
                .phoneNumber(order.getPhoneNumber())
                .zipcode(order.getZipcode())
                .address(order.getAddress())
                .detailAddress(order.getDetailAddress())
                .requestMemo(order.getRequestMemo())
                .cancelReason(order.getCancelReason())
                .cancelReasonType(order.getCancelReasonType())
                .trackingCarrier(order.getTrackingCarrier())
                .trackingNumber(order.getTrackingNumber())
                .pointUsed(order.getPointUsed())
                .pointEarned(order.getPointEarned())
                .deliveryFee(order.getDeliveryFee())
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
