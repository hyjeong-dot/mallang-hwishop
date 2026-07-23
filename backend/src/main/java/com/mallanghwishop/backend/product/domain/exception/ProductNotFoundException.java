package com.mallanghwishop.backend.product.domain.exception;

/**
 * 상품을 찾을 수 없을 때 발생하는 예외
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("상품을 찾을 수 없습니다. (ID: " + id + ")");
    }

    public ProductNotFoundException(String slug) {
        super("상품을 찾을 수 없습니다. (slug: " + slug + ")");
    }
}
