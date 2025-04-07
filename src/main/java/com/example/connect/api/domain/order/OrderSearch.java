package com.example.connect.api.domain.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.*;

import static com.example.connect.api.domain.order.OrderSpec.memberNameLike;
import static com.example.connect.api.domain.order.OrderSpec.orderStatusEq;
import static org.springframework.data.jpa.domain.Specification.where;

@Getter
@Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

    public boolean isOrderStatusSearch() {
        return memberName == null;
    }

    public Specification<Order> toSpecification() {
        return where(memberNameLike(memberName))
                .and(orderStatusEq(orderStatus));
    }
}
