package com.mallanghwishop.backend.member.application.port.out;

public interface DeleteMemberPort {
    void deleteByNickname(String nickname);
    void deleteByUsername(String username);
}
