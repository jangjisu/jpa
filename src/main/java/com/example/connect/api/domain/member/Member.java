package com.example.connect.api.domain.member;

import com.example.connect.api.domain.order.Order;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipCode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;
}
