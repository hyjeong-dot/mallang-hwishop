package com.mallanghwishop.backend.member.domain.model;

import lombok.*;
import java.util.UUID;

/**
 * 회원 도메인 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private UUID id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String phoneNumber;
    private String role; // "ROLE_ADMIN", "ROLE_USER"
    private java.time.LocalDateTime createdAt;

    // --- 비즈니스 로직 ---

    public boolean isAdmin() {
        return "ROLE_ADMIN".equalsIgnoreCase(this.role);
    }
}
