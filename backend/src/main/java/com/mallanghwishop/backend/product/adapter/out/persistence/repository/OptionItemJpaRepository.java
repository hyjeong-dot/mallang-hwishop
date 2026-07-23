package com.mallanghwishop.backend.product.adapter.out.persistence.repository;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.OptionItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionItemJpaRepository extends JpaRepository<OptionItemJpaEntity, Long> {

    List<OptionItemJpaEntity> findAllByOptionIdOrderBySortOrderAsc(Long optionId);

    void deleteAllByOptionId(Long optionId);
}
