package com.mallanghwishop.backend.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import com.mallanghwishop.backend.member.domain.model.Member;

@Entity
@Table(name = "members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String role; // "ROLE_ADMIN", "ROLE_USER"

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
    }

    public static MemberJpaEntity fromDomain(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public Member toDomain() {
        return Member.builder()
                .id(this.id)
                .username(this.username)
                .nickname(this.nickname)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .role(this.role)
                .createdAt(this.createdAt)
                .build();
    }
}
