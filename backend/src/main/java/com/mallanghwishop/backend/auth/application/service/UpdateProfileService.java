package com.mallanghwishop.backend.auth.application.service;

import com.mallanghwishop.backend.auth.application.command.UpdateProfileCommand;
import com.mallanghwishop.backend.auth.application.port.in.UpdateProfileUseCase;
import com.mallanghwishop.backend.auth.domain.exception.AuthenticationFailedException;
import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.application.port.out.SaveMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProfileService implements UpdateProfileUseCase {

    private final LoadMemberPort loadMemberPort;
    private final SaveMemberPort saveMemberPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateProfile(UpdateProfileCommand command) {
        log.info("Updating profile for user: {}", command.getUsername());

        Member member = loadMemberPort.findByUsername(command.getUsername())
                .orElseThrow(() -> {
                    log.warn("Member not found for update: {}", command.getUsername());
                    return new AuthenticationFailedException();
                });

        if (command.getName() != null && !command.getName().isBlank()) {
            member.setName(command.getName());
        }
        
        if (command.getEmail() != null) {
            member.setEmail(command.getEmail());
        }
        
        if (command.getPhoneNumber() != null) {
            member.setPhoneNumber(command.getPhoneNumber());
        }
        
        if (command.getZipcode() != null) {
            member.setZipcode(command.getZipcode());
        }
        if (command.getAddress() != null) {
            member.setAddress(command.getAddress());
        }
        if (command.getDetailAddress() != null) {
            member.setDetailAddress(command.getDetailAddress());
        }
        if (command.getRefundBank() != null) {
            member.setRefundBank(command.getRefundBank());
        }
        if (command.getRefundAccount() != null) {
            member.setRefundAccount(command.getRefundAccount());
        }
        if (command.getRefundHolder() != null) {
            member.setRefundHolder(command.getRefundHolder());
        }

        if (command.getPassword() != null && !command.getPassword().isBlank()) {
            member.setPassword(passwordEncoder.encode(command.getPassword()));
        }

        saveMemberPort.save(member);
        log.info("Profile updated successfully for user: {}", command.getUsername());
    }
}
