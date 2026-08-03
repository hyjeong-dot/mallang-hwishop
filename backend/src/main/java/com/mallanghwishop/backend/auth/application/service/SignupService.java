package com.mallanghwishop.backend.auth.application.service;

import com.mallanghwishop.backend.auth.application.command.SignupCommand;
import com.mallanghwishop.backend.auth.application.port.in.SignupUseCase;
import com.mallanghwishop.backend.auth.application.result.SignupResult;

import com.mallanghwishop.backend.member.application.port.out.SaveMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

    private final SaveMemberPort saveMemberPort;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public SignupResult signup(SignupCommand command) {
        if (saveMemberPort.existsByUsername(command.getUsername())) {
             throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        Member newMember = Member.builder()
                .id(UUID.randomUUID())
                .username(command.getUsername())
                .password(passwordEncoder.encode(command.getPassword()))
                .name(command.getName())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .zipcode(command.getZipcode())
                .address(command.getAddress())
                .detailAddress(command.getDetailAddress())
                .refundBank(command.getRefundBank())
                .refundAccount(command.getRefundAccount())
                .refundHolder(command.getRefundHolder())
                .role("ROLE_USER")
                .build();
        
        Member savedMember = saveMemberPort.save(newMember);
        log.info("Signup successful for user: {}", command.getUsername());

        
        return SignupResult.builder()
                .memberId(savedMember.getId())
                .username(savedMember.getUsername())
                .build();
    }
}

