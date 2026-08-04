package com.mallanghwishop.backend.order.adapter.in.web;

import com.mallanghwishop.backend.admin.order.adapter.in.web.OrderSseEmitters;
import com.mallanghwishop.backend.auth.domain.exception.AuthenticationFailedException;
import com.mallanghwishop.backend.order.adapter.in.web.request.CreateOrderRequest;
import com.mallanghwishop.backend.order.application.port.in.CreateOrderUseCase;
import com.mallanghwishop.backend.order.application.port.in.GetMyOrdersUseCase;
import com.mallanghwishop.backend.order.application.port.in.CancelOrderUseCase;
import com.mallanghwishop.backend.order.application.result.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetMyOrdersUseCase getMyOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final OrderSseEmitters orderSseEmitters;

    @PostMapping
    public OrderResult createOrder(@RequestBody CreateOrderRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AuthenticationFailedException();
        }

        OrderResult result = createOrderUseCase.createOrder(request.toCommand(authentication.getName()));
        // 새 주문 생성 시 관리자 SSE 구독자에게 알림
        orderSseEmitters.notify("new-order");
        return result;
    }

    @GetMapping
    public List<OrderResult> getMyOrders(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AuthenticationFailedException();
        }

        return getMyOrdersUseCase.getMyOrders(authentication.getName());
    }

    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId, @RequestBody(required = false) com.mallanghwishop.backend.order.adapter.in.web.request.CancelOrderRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AuthenticationFailedException();
        }

        if (request == null) {
            request = new com.mallanghwishop.backend.order.adapter.in.web.request.CancelOrderRequest();
        }

        cancelOrderUseCase.cancelOrder(request.toCommand(orderId, authentication.getName()));
    }
}
