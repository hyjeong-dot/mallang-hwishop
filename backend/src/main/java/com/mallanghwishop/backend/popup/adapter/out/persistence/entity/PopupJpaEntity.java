package com.mallanghwishop.backend.popup.adapter.out.persistence.entity;

import com.mallanghwishop.backend.popup.domain.model.Popup;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "popups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PopupJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String linkUrl;

    @Column(nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Popup toDomain() {
        return Popup.builder()
                .id(this.id)
                .title(this.title)
                .imageUrl(this.imageUrl)
                .linkUrl(this.linkUrl)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public static PopupJpaEntity fromDomain(Popup popup) {
        return PopupJpaEntity.builder()
                .id(popup.getId())
                .title(popup.getTitle())
                .imageUrl(popup.getImageUrl())
                .linkUrl(popup.getLinkUrl())
                .isActive(popup.getIsActive() != null ? popup.getIsActive() : false)
                .build();
    }
}
