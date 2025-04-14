package com.example.connect.api.domain.user;

import com.example.connect.api.converter.BooleanToYNConverter;
import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.BaseEntity;
import com.example.connect.api.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "Users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID")
    private Long id;
    private String phoneNum;
    private String name;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean vip;

    @Embedded
    private Address address;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    @ToString.Exclude
    @BatchSize(size = 2)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Article> articles = new HashSet<>();

    public User(String phoneNum, String name) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public static User create(String phoneNum, String name) {
        return new User(phoneNum, name);
    }

    public User(String phoneNum, String name, Address address) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public static User create(String phoneNum, String name, Address address) {
        return new User(phoneNum, name, address);
    }
}


