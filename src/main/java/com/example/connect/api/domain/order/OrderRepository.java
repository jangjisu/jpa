package com.example.connect.api.domain.order;

import com.example.connect.api.domain.CreateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, CreateRepository<Order> {
    List<Order> findByOrderStatus(OrderStatus orderStatus);

    @Query("select o from Order o join o.member m where m.name = :memberName")
    List<Order> findByMemberName(String memberName);
}
