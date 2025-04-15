package com.example.connect.api.domain.user;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, CreateRepository<User> {
    @Query("select distinct u from User u left join u.articles left join u.orders")
    @EntityGraph(attributePaths = {"articles", "orders"}, type = EntityGraph.EntityGraphType.FETCH)
    List<User> findAllWithTwoEntities();

    @Query("select distinct u from User u left join u.articles")
    List<User> findAllJPQL();
}
