package com.example.connect.api.domain.order;

import java.util.List;

public interface CustomOrderRepository {
    public List<Order> search (OrderSearch orderSearch);
}
