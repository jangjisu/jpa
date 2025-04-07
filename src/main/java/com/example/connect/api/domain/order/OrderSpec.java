package com.example.connect.api.domain.order;

import com.example.connect.api.domain.member.Member;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpec {
    public static Specification<Order> memberNameLike(final String memberName) {
        return (root, query, builder) -> {
            if (StringUtils.isEmpty(memberName)) {
                return null;
            }
            Join<Order, Member> m = root.join("member", JoinType.INNER);
            return builder.like(m.<String>get("name"), "%" + memberName + "%");
        };
    }

    public static Specification<Order> orderStatusEq(final OrderStatus orderStatus) {
        return (root, query, builder) -> {
           if (orderStatus == null) {
               return null;
           }
           return builder.equal(root.get("status"), orderStatus);
        };
    }
}
