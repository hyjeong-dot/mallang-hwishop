package com.mallanghwishop.backend.order.application.service;



import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.order.application.port.in.CreateOrderUseCase;
import com.mallanghwishop.backend.order.application.port.in.command.CreateOrderCommand;
import com.mallanghwishop.backend.order.application.port.out.OrderPort;
import com.mallanghwishop.backend.order.application.result.OrderResult;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.order.domain.model.OrderLineItem;
import com.mallanghwishop.backend.order.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderPort orderPort;
    private final LoadProductPort loadProductPort;
    private final com.mallanghwishop.backend.product.application.port.out.SaveProductPort saveProductPort;
    private final LoadMemberPort loadMemberPort;
    private final com.mallanghwishop.backend.point.application.port.in.PointUseCase pointUseCase;

    @Override
    @Transactional
    public OrderResult createOrder(CreateOrderCommand command) {

        Member member = loadMemberPort.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        String orderUid = "ORDER-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);

        Order order = Order.builder()
                .memberId(member.getId())
                .recipientName(command.getRecipientName())
                .phoneNumber(command.getPhoneNumber())
                .zipcode(command.getZipcode())
                .address(command.getAddress())
                .detailAddress(command.getDetailAddress())
                .requestMemo(command.getRequestMemo())
                .orderUid(orderUid)
                .status(OrderStatus.PENDING)
                .pointUsed(command.getPointUsed() != null ? command.getPointUsed() : 0)
                .build();

        command.getItems().forEach(itemCmd -> {
            Product product = loadProductPort.findAvailableByIdWithLock(itemCmd.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없거나 판매가 중지되었습니다: " + itemCmd.getProductId()));

            if (product.getMaxPerOrder() != null && itemCmd.getQuantity() > product.getMaxPerOrder()) {
                throw new IllegalStateException(product.getKorName() + "의 최대 주문 가능 수량은 " + product.getMaxPerOrder() + "개입니다.");
            }

            product.decreaseStock(itemCmd.getQuantity());
            saveProductPort.save(product);

            // 할인가가 적용된 상품인지 확인
            boolean isDiscounted = (product.getDiscountPrice() != null && product.getDiscountPrice() > 0 && product.getDiscountPrice() < product.getPrice());
            
            // 최종 단가 결정 (할인가가 있으면 할인가 우선, 없으면 정가)
            int finalPrice = isDiscounted ? product.getDiscountPrice() : product.getPrice();
            
            // 옵션 등 추가 금액이 포함된 단가가 넘어온 경우
            if (itemCmd.getUnitPrice() != null && itemCmd.getUnitPrice() > finalPrice) {
                finalPrice = itemCmd.getUnitPrice();
            }

            // 적립금 계산 (정가 판매 상품만 3% 적립)
            int pointEarned = 0;
            if (!isDiscounted) {
                // 정가 판매 상품인 경우 3% 적립
                pointEarned = (int) Math.floor((finalPrice * itemCmd.getQuantity()) * 0.03);
            }

            OrderLineItem lineItem = OrderLineItem.builder()
                    .productId(product.getId())
                    .price(finalPrice)
                    .quantity(itemCmd.getQuantity())
                    .pointEarned(pointEarned)
                    .build();

            order.addLineItem(lineItem);
        });

        order.calculateTotalPrice();

        // 주문의 총 적립 예정 포인트 계산
        int totalPointEarned = order.getItems().stream().mapToInt(OrderLineItem::getPointEarned).sum();
        order.setPointEarned(totalPointEarned);

        // 배송비 처리는 Phase 3 나중에 추가. 여기서는 상품 총합 + 배송비(예: 3500)에서 포인트를 차감해야 함.
        // 현재는 상품 총합(totalPrice)에서 포인트 차감 처리. (음수 방지)
        int pointUsed = order.getPointUsed();
        if (pointUsed > 0) {
            order.applyDiscount(pointUsed); // applyDiscount는 totalPrice에서 차감함
        }

        Order savedOrder = orderPort.saveOrder(order);

        // 포인트 차감 적용 (DB 저장 후)
        if (pointUsed > 0) {
            pointUseCase.usePoints(
                    member.getId(),
                    pointUsed,
                    savedOrder.getId(),
                    "주문 #" + savedOrder.getOrderUid() + " 결제 사용"
            );
        }

        return OrderResult.builder()
                .orderId(savedOrder.getId())
                .orderUid(savedOrder.getOrderUid())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }
}
