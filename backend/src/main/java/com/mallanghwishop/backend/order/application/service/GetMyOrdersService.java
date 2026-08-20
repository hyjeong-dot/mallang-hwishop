package com.mallanghwishop.backend.order.application.service;

import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductJpaRepository;
import com.mallanghwishop.backend.order.application.port.in.GetMyOrdersUseCase;
import com.mallanghwishop.backend.order.application.port.out.OrderPort;
import com.mallanghwishop.backend.order.application.result.OrderLineItemResult;
import com.mallanghwishop.backend.order.application.result.OrderResult;
import com.mallanghwishop.backend.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMyOrdersService implements GetMyOrdersUseCase {

    private final OrderPort orderPort;
    private final LoadMemberPort loadMemberPort;
    private final ProductJpaRepository productJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResult> getMyOrders(String username) {
        Member member = loadMemberPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        List<Order> orders = orderPort.findAllByMemberIdOrderByCreatedAtDesc(member.getId());

        // 모든 주문의 상품 ID를 모아서 한번에 조회 (N+1 방지)
        Set<Long> productIds = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(item -> item.getProductId())
                .collect(Collectors.toSet());

        Map<Long, ProductJpaEntity> productMap = productJpaRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductJpaEntity::getId, m -> m));

        return orders.stream()
                .map(order -> {
                    List<OrderLineItemResult> itemResults = order.getItems().stream()
                            .map(item -> {
                                ProductJpaEntity product = productMap.get(item.getProductId());
                                return OrderLineItemResult.builder()
                                        .productId(item.getProductId())
                                        .productName(product != null ? product.getKorName() : "삭제된 상품")
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return OrderResult.builder()
                            .orderId(order.getId())
                            .orderUid(order.getOrderUid())
                            .totalPrice(order.getTotalPrice())
                            .recipientName(order.getRecipientName())
                            .phoneNumber(order.getPhoneNumber())
                            .zipcode(order.getZipcode())
                            .address(order.getAddress())
                            .detailAddress(order.getDetailAddress())
                            .status(order.getStatus())
                            .createdAt(order.getCreatedAt())
                            .items(itemResults)
                            .requestMemo(order.getRequestMemo())
                            .cancelReason(order.getCancelReason())
                            .cancelReasonType(order.getCancelReasonType())
                            .trackingCarrier(order.getTrackingCarrier())
                            .trackingNumber(order.getTrackingNumber())
                            .pointUsed(order.getPointUsed())
                            .pointEarned(order.getPointEarned())
                            .deliveryFee(order.getDeliveryFee())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

