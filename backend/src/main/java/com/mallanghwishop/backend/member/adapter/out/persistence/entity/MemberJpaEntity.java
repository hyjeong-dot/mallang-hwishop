package com.mallanghwishop.backend.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import com.mallanghwishop.backend.member.domain.model.Member;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJpaEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String zipcode;
    private String address;
    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "refund_bank")
    private String refundBank;
    @Column(name = "refund_account")
    private String refundAccount;
    @Column(name = "refund_holder")
    private String refundHolder;

    private String role; // "ROLE_ADMIN", "ROLE_USER"

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
    }

    @Override
    public boolean isNew() {
        return this.createdAt == null;
    }

    public static MemberJpaEntity fromDomain(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .password(member.getPassword())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .zipcode(member.getZipcode())
                .address(member.getAddress())
                .detailAddress(member.getDetailAddress())
                .refundBank(member.getRefundBank())
                .refundAccount(member.getRefundAccount())
                .refundHolder(member.getRefundHolder())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public Member toDomain() {
        return Member.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .zipcode(this.zipcode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .refundBank(this.refundBank)
                .refundAccount(this.refundAccount)
                .refundHolder(this.refundHolder)
                .role(this.role)
                .createdAt(this.createdAt)
                .build();
    }
}
