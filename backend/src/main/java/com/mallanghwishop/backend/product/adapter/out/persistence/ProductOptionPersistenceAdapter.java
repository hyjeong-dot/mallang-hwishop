package com.mallanghwishop.backend.product.adapter.out.persistence;

import com.mallanghwishop.backend.product.adapter.out.persistence.entity.OptionItemJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.entity.ProductOptionJpaEntity;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.OptionItemJpaRepository;
import com.mallanghwishop.backend.product.adapter.out.persistence.repository.ProductOptionJpaRepository;
import com.mallanghwishop.backend.product.application.port.out.ProductOptionPort;
import com.mallanghwishop.backend.product.domain.model.OptionItem;
import com.mallanghwishop.backend.product.domain.model.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductOptionPersistenceAdapter implements ProductOptionPort {

    private final ProductOptionJpaRepository optionRepository;
    private final OptionItemJpaRepository itemRepository;

    @Override
    public List<ProductOption> findAllByProductId(Long productId) {
        return optionRepository.findAllByProductIdOrderBySortOrderAsc(productId)
                .stream().map(ProductOptionJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OptionItem> findAllByOptionId(Long optionId) {
        return itemRepository.findAllByOptionIdOrderBySortOrderAsc(optionId)
                .stream().map(OptionItemJpaEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public ProductOption saveOption(ProductOption option) {
        return optionRepository.save(ProductOptionJpaEntity.fromDomain(option)).toDomain();
    }

    @Override
    public OptionItem saveItem(OptionItem item) {
        return itemRepository.save(OptionItemJpaEntity.fromDomain(item)).toDomain();
    }

    @Override
    public void deleteAllByProductId(Long productId) {
        List<ProductOptionJpaEntity> options = optionRepository.findAllByProductIdOrderBySortOrderAsc(productId);
        for (ProductOptionJpaEntity opt : options) {
            itemRepository.deleteAllByOptionId(opt.getId());
        }
        optionRepository.deleteAllByProductId(productId);
    }

    @Override
    public void deleteAllItemsByOptionId(Long optionId) {
        itemRepository.deleteAllByOptionId(optionId);
    }
}
