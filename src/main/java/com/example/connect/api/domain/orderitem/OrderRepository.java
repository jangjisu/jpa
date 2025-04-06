package com.example.connect.api.domain.orderitem;

import com.example.connect.api.domain.CreateRepository;
import com.example.connect.api.domain.order.Order;
import com.example.connect.api.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, CreateRepository<Order> {
    List<Order> findByOrderStatus(OrderStatus orderStatus);

    @Query("select o from Order o join o.member m where m.name = :memberName")
    List<Order> findByMemberName(String memberName);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findWithItemsById(Long id);
}
