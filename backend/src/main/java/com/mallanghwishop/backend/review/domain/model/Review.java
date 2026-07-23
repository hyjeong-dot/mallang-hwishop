package com.mallanghwishop.backend.review.domain.model;

import com.mallanghwishop.backend.order.domain.model.Order;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private Long id;
    private UUID memberId;
    @ToString.Exclude
    private Order order;
    private String content;
    private int rating; // 1~5
    private LocalDateTime createdAt;

    /** 하위 호환용 */
    public Long getOrderId() {
        return order != null ? order.getId() : null;
    }
}

