package com.mallanghwishop.backend.admin.noticeimage.adapter.out.persistence.entity;

import com.mallanghwishop.backend.admin.noticeimage.domain.model.NoticeImage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public static NoticeImageJpaEntity fromDomain(NoticeImage noticeImage) {
        return NoticeImageJpaEntity.builder()
                .id(noticeImage.getId())
                .url(noticeImage.getUrl())
                .sortOrder(noticeImage.getSortOrder())
                .createdAt(noticeImage.getCreatedAt())
                .build();
    }

    public NoticeImage toDomain() {
        return NoticeImage.builder()
                .id(this.id)
                .url(this.url)
                .sortOrder(this.sortOrder)
                .createdAt(this.createdAt)
                .build();
    }
}
