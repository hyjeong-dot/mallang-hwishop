package com.mallanghwishop.backend.order.application.service;



import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.menu.application.port.out.LoadMenuPort;
import com.mallanghwishop.backend.menu.domain.model.Menu;
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
    private final LoadMenuPort loadMenuPort;
    private final LoadMemberPort loadMemberPort;

    private final com.mallanghwishop.backend.admin.cafe.application.port.in.GetCafeSettingsUseCase getCafeSettingsUseCase;

    @Override
    @Transactional
    public OrderResult createOrder(CreateOrderCommand command) {
        if (!getCafeSettingsUseCase.getSettings().isOpen()) {
            throw new IllegalStateException("현재 진행 중인 영업 시간이 아닙니다. 내일 이용해 주세요!");
        }

        Member member = loadMemberPort.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        String orderUid = "ORDER-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);

        Order order = Order.builder()
                .memberId(member.getId())
                .orderType(command.getOrderType())
                .requestMemo(command.getRequestMemo())
                .orderUid(orderUid)
                .status(OrderStatus.PENDING)
                .build();

        command.getItems().forEach(itemCmd -> {
            Menu menu = loadMenuPort.findAvailableById(itemCmd.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu not found or not available: " + itemCmd.getMenuId()));

            // 옵션 포함 단가가 전달되면 사용, 없으면 메뉴 기본가
            int price = menu.getPrice();
            if (itemCmd.getUnitPrice() != null && itemCmd.getUnitPrice() >= menu.getPrice()) {
                price = itemCmd.getUnitPrice();
            }

            OrderLineItem lineItem = OrderLineItem.builder()
                    .menuId(menu.getId())
                    .price(price)
                    .quantity(itemCmd.getQuantity())
                    .build();

            order.addLineItem(lineItem);
        });

        order.calculateTotalPrice();

        // Phase 2에서 적립금 적립 로직 추가 예정
        Order savedOrder = orderPort.saveOrder(order);


        return OrderResult.builder()
                .orderId(savedOrder.getId())
                .orderUid(savedOrder.getOrderUid())
                .totalPrice(savedOrder.getTotalPrice())
                .orderType(savedOrder.getOrderType())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }
}
