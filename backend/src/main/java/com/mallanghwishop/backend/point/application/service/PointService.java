package com.mallanghwishop.backend.point.application.service;

import com.mallanghwishop.backend.point.application.port.in.PointUseCase;
import com.mallanghwishop.backend.point.application.port.out.LoadPointHistoryPort;
import com.mallanghwishop.backend.point.application.port.out.SavePointHistoryPort;
import com.mallanghwishop.backend.point.domain.model.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService implements PointUseCase {

    private final SavePointHistoryPort savePointHistoryPort;
    private final LoadPointHistoryPort loadPointHistoryPort;

    @Override
    @Transactional
    public void earnPoints(UUID memberId, int amount, Long orderId, String description) {
        if (amount <= 0) return;
        PointHistory history = PointHistory.createEarn(memberId, amount, orderId, description);
        savePointHistoryPort.save(history);
    }

    @Override
    @Transactional
    public void usePoints(UUID memberId, int amount, Long orderId, String description) {
        if (amount <= 0) return;
        
        int balance = loadPointHistoryPort.getBalance(memberId);
        if (balance < amount) {
            throw new IllegalArgumentException("적립금이 부족합니다.");
        }
        
        PointHistory history = PointHistory.createUse(memberId, amount, orderId, description);
        savePointHistoryPort.save(history);
    }

    @Override
    public int getBalance(UUID memberId) {
        return loadPointHistoryPort.getBalance(memberId);
    }

    @Override
    public List<PointHistory> getHistories(UUID memberId) {
        return loadPointHistoryPort.loadByMemberId(memberId);
    }
}
