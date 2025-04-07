package com.example.connect.api.domain.order;

import com.example.connect.api.domain.member.QMember;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

public class CustomOrderRepositoryImpl extends QuerydslRepositorySupport implements CustomOrderRepository {


    public CustomOrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> search (OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPQLQuery<Order> query= from(order);

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.leftJoin(order.member, member)
                    .where(member.name.contains(orderSearch.getMemberName()));
        }

        if (orderSearch.getOrderStatus() != null) {
            query.where(order.orderStatus.eq(orderSearch.getOrderStatus()));
        }

        return query.select(order).fetch();
    }
}
