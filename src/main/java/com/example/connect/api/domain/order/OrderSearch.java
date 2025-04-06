package com.example.connect.api.domain.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

    public boolean isOrderStatusSearch() {
        return memberName == null;
    }
}
