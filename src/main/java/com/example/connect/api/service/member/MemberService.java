package com.example.connect.api.service.member;

import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member) {

        if (member.getId() != null) {
            validateDuplicateMember(member);
        }

        Member save = memberRepository.create(member);
        return save.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }
}
