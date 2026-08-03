package com.mallanghwishop.backend.point.adapter.in.web;

import com.mallanghwishop.backend.point.application.port.in.PointUseCase;
import com.mallanghwishop.backend.point.domain.model.PointHistory;
import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import com.mallanghwishop.backend.auth.domain.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {

    private final PointUseCase pointUseCase;
    private final LoadMemberPort loadMemberPort;

    @GetMapping("/history")
    public List<PointHistory> getMyPointHistories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AuthenticationFailedException();
        }

        String username = auth.getName();
        Member member = loadMemberPort.findByUsername(username)
                .orElseThrow(AuthenticationFailedException::new);

        return pointUseCase.getHistories(member.getId());
    }
}
