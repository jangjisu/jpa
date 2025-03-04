package com.example.connect.api.domain.delivery;

import com.example.connect.api.domain.order.Order;
import jakarta.persistence.*;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    private String street;
    private String zipCode;
    private String status;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
