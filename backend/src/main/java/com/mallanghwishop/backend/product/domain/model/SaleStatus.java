package com.mallanghwishop.backend.product.domain.model;

/**
 * 상품 판매 상태
 * - UPCOMING: 판매 시작 전 (예약 대기, 카운트다운 표시)
 * - ON_SALE: 판매 중
 * - SOLD_OUT: 품절
 */
public enum SaleStatus {
    UPCOMING,
    ON_SALE,
    SOLD_OUT
}
