package com.book.jpa.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userTelNo;
    private String userName;


    private Member(String userTelNo, String userName) {
        this.userTelNo = userTelNo;
        this.userName = userName;
    }

    public static Member create(String userTelNo, String userName) {
        return new Member(userTelNo, userName);
    }
}

