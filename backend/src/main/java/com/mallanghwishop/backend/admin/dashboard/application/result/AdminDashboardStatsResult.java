package com.mallanghwishop.backend.admin.dashboard.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardStatsResult {
    private final long totalProducts;
    private final long todayOrders;
    private final long todaySales;
    private final long todayVisits;
}
