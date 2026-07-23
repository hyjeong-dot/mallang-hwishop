package com.mallanghwishop.backend.admin.dashboard.application.service;

import com.mallanghwishop.backend.admin.dashboard.application.port.in.GetAdminDashboardStatsUseCase;
import com.mallanghwishop.backend.admin.dashboard.application.result.AdminDashboardStatsResult;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductJpaRepository;
import com.mallanghwishop.backend.order.adapter.out.persistence.repository.OrderRepository;
import com.mallanghwishop.backend.order.domain.model.Order;
import com.mallanghwishop.backend.member.adapter.out.persistence.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAdminDashboardStatsService implements GetAdminDashboardStatsUseCase {

    private final ProductJpaRepository productRepository;
    private final OrderRepository orderRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public AdminDashboardStatsResult getStats() {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);

        long totalMenus = productRepository.count();
        
        List<Order> todayOrdersList = orderRepository.findAllByCreatedAtAfter(todayStart);
        long todayOrders = todayOrdersList.size();
        long todaySales = todayOrdersList.stream()
                .mapToLong(Order::getTotalPrice)
                .sum();
        
        long todayVisits = memberRepository.countByCreatedAtAfter(todayStart);

        return AdminDashboardStatsResult.builder()
                .totalMenus(totalMenus)
                .todayOrders(todayOrders)
                .todaySales(todaySales)
                .todayVisits(todayVisits)
                .build();
    }
}
