package com.example.connect.api.domain.member;

import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.BaseEntity;
import com.example.connect.api.domain.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "MEMBER_ID")
    private Long id;
    private String phoneNum;
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String phoneNum, String name) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public static Member create(String phoneNum, String name) {
        return new Member(phoneNum, name);
    }

    public Member(String phoneNum, String name, Address address) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public static Member create(String phoneNum, String name, Address address) {
        return new Member(phoneNum, name, address);
    }
}
