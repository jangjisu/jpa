package com.example.connect.api.domain.member;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, CreateRepository<Member> {
    List<Member> findByName(String name);

    List<Member> findByPhoneNum(String phoneNum);

    @Query("select distinct u from Member u left join u.articles")
    List<Member> findAllJPQL();

    @Query("select distinct u from Member u left join u.articles")
    @EntityGraph(attributePaths = {"articles"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Member> findAllJPQLEntityGraphFetch();

    @Query("select distinct u from Member u left join u.articles")
    @EntityGraph(attributePaths = {"articles"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Member> findAllPage(Pageable pageable);

    @Query("select distinct u from Member u left join fetch u.articles")
    List<Member> findAllJPQLFetch();

    @Query("select distinct u from Member u left join u.articles left join u.orders")
    @EntityGraph(attributePaths = {"articles", "orders"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Member> findAllWithTwoEntities();
}
