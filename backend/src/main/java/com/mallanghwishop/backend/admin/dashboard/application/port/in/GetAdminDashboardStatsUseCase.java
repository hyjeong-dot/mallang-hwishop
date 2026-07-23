package com.mallanghwishop.backend.admin.dashboard.application.port.in;

import com.mallanghwishop.backend.admin.dashboard.application.result.AdminDashboardStatsResult;

public interface GetAdminDashboardStatsUseCase {
    AdminDashboardStatsResult getStats();
}
