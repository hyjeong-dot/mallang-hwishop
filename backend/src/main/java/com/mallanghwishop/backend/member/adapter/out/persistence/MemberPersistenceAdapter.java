package com.mallanghwishop.backend.member.adapter.out.persistence;

import com.mallanghwishop.backend.member.application.port.out.LoadMemberPort;
import com.mallanghwishop.backend.member.application.port.out.SaveMemberPort;
import com.mallanghwishop.backend.member.application.port.out.DeleteMemberPort;
import com.mallanghwishop.backend.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;
import com.mallanghwishop.backend.member.adapter.out.persistence.entity.MemberJpaEntity;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements LoadMemberPort, SaveMemberPort, DeleteMemberPort {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname).map(MemberJpaEntity::toDomain);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username).map(MemberJpaEntity::toDomain);
    }

    @Override
    public Optional<Member> findById(UUID memberId) {
        return memberJpaRepository.findById(memberId).map(MemberJpaEntity::toDomain);
    }

    @Override
    public Member save(Member member) {
        MemberJpaEntity entity = MemberJpaEntity.fromDomain(member);
        return memberJpaRepository.save(entity).toDomain();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByUsername(String username) {
        return memberJpaRepository.existsByUsername(username);
    }

    @Override
    public void deleteByNickname(String nickname) {
        memberJpaRepository.deleteByNickname(nickname);
    }

    @Override
    public void deleteByUsername(String username) {
        memberJpaRepository.deleteByUsername(username);
    }
}
