package com.mallanghwishop.backend.order.domain.model;

public enum OrderStatus {
    PENDING,     // 결제 대기
    PAID,        // 결제 완료
    PREPARING,   // 배송 준비 중
    COMPLETED,   // 배송 완료
    CANCELLED    // 취소됨
}
