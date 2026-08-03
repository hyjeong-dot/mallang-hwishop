package com.mallanghwishop.backend.point.application.port.out;

import com.mallanghwishop.backend.point.domain.model.PointHistory;
import java.util.List;
import java.util.UUID;

public interface LoadPointHistoryPort {
    List<PointHistory> loadByMemberId(UUID memberId);
    int getBalance(UUID memberId);
}
