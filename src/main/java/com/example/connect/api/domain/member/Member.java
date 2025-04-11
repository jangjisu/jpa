package com.example.connect.api.domain.member;

import com.example.connect.api.converter.BooleanToYNConverter;
import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.BaseEntity;
import com.example.connect.api.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "MEMBER_ID")
    private Long id;
    private String phoneNum;
    private String name;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean vip;

    @Embedded
    private Address address;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

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
