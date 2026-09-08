package com.mallanghwishop.backend.admin.noticeimage.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NoticeImage {
    private Long id;
    private String url;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
