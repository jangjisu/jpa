package com.example.connect.api.service.member;

import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원가입 테스트")
    @Test
    void 회원가입() {
        // given
        Member member = Member.create("01012345678", "홍길동");

        // when
        Long savedId = memberService.join(member);

        // then
        Long id = memberRepository.findById(savedId).get().getId();
        assertThat(savedId).isEqualTo(id);
    }

    @DisplayName("")
    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = Member.create("01012345678", "홍길동");
        Long savedId = memberService.join(member1);

        // when
        Member member2 = Member.create("01012345678", "홍길동");
        member2.setId(savedId);
        // then
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원입니다.");
    }

}