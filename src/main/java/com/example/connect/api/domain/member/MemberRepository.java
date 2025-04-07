package com.example.connect.api.domain.member;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, CreateRepository<Member> {
    List<Member> findByName(String name);

    List<Member> findByPhoneNum(String phoneNum);
}
