package com.mallanghwishop.backend.auth.application.service;

import com.mallanghwishop.backend.auth.application.command.LoginCommand;
import com.mallanghwishop.backend.auth.application.port.in.LoginUseCase;
import com.mallanghwishop.backend.auth.application.result.LoginResult;
import com.mallanghwishop.backend.auth.domain.exception.AuthenticationFailedException;
import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService implements LoginUseCase {

    private final LoadMemberPort loadMemberPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResult login(LoginCommand command) {
        log.info("Login attempt for username: {}", command.getUsername());
        
        Member member = loadMemberPort.findByUsername(command.getUsername())
                .orElseThrow(() -> {
                    log.warn("Member not found: {}", command.getUsername());
                    return new AuthenticationFailedException();
                });

        log.info("Member found, checking password for user: {}", member.getUsername());

        if (!passwordEncoder.matches(command.getPassword(), member.getPassword())) {
            log.warn("Password mismatch for user: {}", command.getUsername());
            throw new AuthenticationFailedException();
        }

        log.info("Login successful for user: {}", command.getUsername());

        return LoginResult.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
