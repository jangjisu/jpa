package com.example.connect.api.domain.orderitem;

import com.example.connect.api.domain.orderitem.key.OrderItemKey;
import jakarta.persistence.*;

@Entity
public class OrderItem {
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private OrderItemKey id;

    private int orderPrice;
    private int count;
}
