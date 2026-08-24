package com.mallanghwishop.backend.popup.adapter.out.persistence;

import com.mallanghwishop.backend.popup.adapter.out.persistence.entity.PopupJpaEntity;
import com.mallanghwishop.backend.popup.application.port.out.DeletePopupPort;
import com.mallanghwishop.backend.popup.application.port.out.LoadPopupPort;
import com.mallanghwishop.backend.popup.application.port.out.SavePopupPort;
import com.mallanghwishop.backend.popup.domain.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PopupPersistenceAdapter implements LoadPopupPort, SavePopupPort, DeletePopupPort {

    private final PopupJpaRepository popupJpaRepository;

    @Override
    public List<Popup> findAllActive() {
        return popupJpaRepository.findByIsActiveTrueOrderByIdDesc().stream()
                .map(PopupJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Popup> findAll() {
        return popupJpaRepository.findAllByOrderByIdDesc().stream()
                .map(PopupJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Popup> findById(Long id) {
        return popupJpaRepository.findById(id).map(PopupJpaEntity::toDomain);
    }

    @Override
    public Popup save(Popup popup) {
        PopupJpaEntity entity = PopupJpaEntity.fromDomain(popup);
        PopupJpaEntity savedEntity = popupJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        popupJpaRepository.deleteById(id);
    }
}
