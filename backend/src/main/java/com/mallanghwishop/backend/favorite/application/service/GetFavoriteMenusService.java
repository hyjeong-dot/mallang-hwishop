package com.mallanghwishop.backend.favorite.application.service;

import com.mallanghwishop.backend.category.application.port.out.LoadCategoryPort;
import com.mallanghwishop.backend.category.domain.model.Category;
import com.mallanghwishop.backend.favorite.application.port.in.GetFavoriteMenusUseCase;
import com.mallanghwishop.backend.favorite.application.port.out.LoadFavoriteListPort;
import com.mallanghwishop.backend.favorite.application.result.FavoriteMenuListResult;
import com.mallanghwishop.backend.favorite.application.result.FavoriteMenuResult;
import com.mallanghwishop.backend.favorite.domain.model.Favorite;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.product.domain.model.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetFavoriteMenusService implements GetFavoriteMenusUseCase {

    private final LoadFavoriteListPort loadFavoriteListPort;
    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;
    private final LoadCategoryPort loadCategoryPort;

    @Override
    public FavoriteMenuListResult getFavoriteMenus(UUID memberId) {
        List<Favorite> favorites = loadFavoriteListPort.findFavoritesByMemberId(memberId);
        List<Category> categories = loadCategoryPort.findAllActive();
        
        List<FavoriteMenuResult> menus = favorites.stream()
                .map(favorite -> loadProductPort.findAvailableById(favorite.getMenuId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(product -> {
                    // 카테고리 정보 매핑
                    String categoryName = "";
                    String categoryIcon = "";
                    for (Category cat : categories) {
                        if (cat.getId().equals(product.getCategoryId())) {
                            categoryName = cat.getName();
                            categoryIcon = cat.getIcon();
                            break;
                        }
                    }

                    // 이미지 (첫 번째)
                    List<ProductImage> images = productImagePort.findAllByProductId(product.getId());
                    String imageSrc = images.isEmpty() ? "blank.png" : images.get(0).getSrcUrl();

                    return FavoriteMenuResult.builder()
                            .id(product.getId())
                            .korName(product.getKorName())
                            .engName(product.getEngName())
                            .price(product.getPrice())
                            .description(product.getDescription())
                            .categoryId(product.getCategoryId())
                            .categoryName(categoryName)
                            .categoryIcon(categoryIcon)
                            .imageSrc(imageSrc)
                            .isSoldOut(Boolean.TRUE.equals(product.getIsSoldOut()))
                            .build();
                })
                .collect(Collectors.toList());
                
        return FavoriteMenuListResult.builder().menus(menus).build();
    }
}
