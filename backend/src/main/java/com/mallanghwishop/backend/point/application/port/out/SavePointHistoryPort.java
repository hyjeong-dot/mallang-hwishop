package com.mallanghwishop.backend.point.application.port.out;

import com.mallanghwishop.backend.point.domain.model.PointHistory;

public interface SavePointHistoryPort {
    void save(PointHistory pointHistory);
}
