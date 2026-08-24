package com.mallanghwishop.backend.popup.adapter.out.persistence;

import com.mallanghwishop.backend.popup.adapter.out.persistence.entity.PopupJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupJpaRepository extends JpaRepository<PopupJpaEntity, Long> {
    List<PopupJpaEntity> findByIsActiveTrueOrderByIdDesc();
    List<PopupJpaEntity> findAllByOrderByIdDesc();
}
