package com.mallanghwishop.backend.cart.application.service;

import com.mallanghwishop.backend.cart.application.port.in.GetCartItemsUseCase;
import com.mallanghwishop.backend.cart.application.port.out.CartPersistencePort;
import com.mallanghwishop.backend.cart.application.result.CartItemResult;
import com.mallanghwishop.backend.cart.domain.model.Cart;
import com.mallanghwishop.backend.product.application.port.out.ProductImagePort;
import com.mallanghwishop.backend.product.application.port.out.LoadProductPort;
import com.mallanghwishop.backend.product.domain.model.Product;
import com.mallanghwishop.backend.product.domain.model.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GetCartItemsService implements GetCartItemsUseCase {

    private final CartPersistencePort cartPersistencePort;
    private final LoadProductPort loadProductPort;
    private final ProductImagePort productImagePort;

    @Override
    @Transactional
    public List<CartItemResult> getCartItems(UUID memberId) {
        Cart cart = cartPersistencePort.findByMemberId(memberId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().memberId(memberId).build();
                    return cartPersistencePort.save(newCart);
                });
        
        return cart.getItems().stream().map(item -> {
            Optional<Product> productOpt = loadProductPort.findAvailableById(item.getProductId());
            if (productOpt.isEmpty()) return null;
            
            Product product = productOpt.get();
            List<ProductImage> images = productImagePort.findAllByProductId(product.getId());
            String imageUrl = images.isEmpty() ? null : images.get(0).getSrcUrl();

            // 옵션 이름 역직렬화 (쉼표 구분)
            List<String> optionNames = Collections.emptyList();
            if (item.getSelectedOptionNames() != null && !item.getSelectedOptionNames().isBlank()) {
                optionNames = Arrays.asList(item.getSelectedOptionNames().split(","));
            }

            // 가격: unitPrice가 있으면 사용, 없으면 상품 기본 가격
            int price = item.getUnitPrice() != null ? item.getUnitPrice() : product.getPrice();

            return CartItemResult.builder()
                    .id(String.valueOf(item.getId()))
                    .productId(product.getId())
                    .korName(product.getKorName())
                    .engName(product.getEngName())
                    .price(price)
                    .quantity(item.getQuantity())
                    .image(imageUrl)
                    .selectedOptionNames(optionNames.isEmpty() ? null : optionNames)
                    .build();
        }).filter(item -> item != null).collect(Collectors.toList());
    }
}
