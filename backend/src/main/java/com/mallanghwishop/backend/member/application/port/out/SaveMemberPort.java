package com.mallanghwishop.backend.member.application.port.out;

import com.mallanghwishop.backend.member.domain.model.Member;

public interface SaveMemberPort {
    Member save(Member member);
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);
}
