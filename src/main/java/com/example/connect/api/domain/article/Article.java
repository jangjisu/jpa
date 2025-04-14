package com.example.connect.api.domain.article;

import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private Article(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        member.getArticles().add(this);
    }

    public static Article create(String title, String content, Member member) {
        return new Article(title, content, member);
    }

    public void setUser (User user) {
        if (this.user != null) {
            this.user.getArticles().remove(this);
        }
        if (user != null) {
            this.user = user;
            user.getArticles().add(this);
        }
    }
}
