package com.mallanghwishop.backend.point.application.port.in;

import com.mallanghwishop.backend.point.domain.model.PointHistory;
import java.util.List;
import java.util.UUID;

public interface PointUseCase {
    void earnPoints(UUID memberId, int amount, Long orderId, String description);
    void usePoints(UUID memberId, int amount, Long orderId, String description);
    int getBalance(UUID memberId);
    List<PointHistory> getHistories(UUID memberId);
}
