package com.mallanghwishop.backend.point.adapter.out.persistence;

import com.mallanghwishop.backend.point.application.port.out.LoadPointHistoryPort;
import com.mallanghwishop.backend.point.application.port.out.SavePointHistoryPort;
import com.mallanghwishop.backend.point.domain.model.PointHistory;
import com.mallanghwishop.backend.point.adapter.out.persistence.entity.PointHistoryJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PointHistoryPersistenceAdapter implements SavePointHistoryPort, LoadPointHistoryPort {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void save(PointHistory pointHistory) {
        pointHistoryRepository.save(PointHistoryJpaEntity.fromDomain(pointHistory));
    }

    @Override
    public List<PointHistory> loadByMemberId(UUID memberId) {
        return pointHistoryRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(PointHistoryJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int getBalance(UUID memberId) {
        return pointHistoryRepository.calculateValidPointBalance(memberId);
    }
}
