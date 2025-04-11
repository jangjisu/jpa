package com.example.connect.api.domain.member;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, CreateRepository<Member> {
    List<Member> findByName(String name);

    List<Member> findByPhoneNum(String phoneNum);

    @Query("select distinct u from Member u left join u.articles")
    List<Member> findAllJPQL();
    @Query("select distinct u from Member u left join fetch u.articles")
    List<Member> findAllJPQLFetch();
}
