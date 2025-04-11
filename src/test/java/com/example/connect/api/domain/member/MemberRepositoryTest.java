package com.example.connect.api.domain.member;

import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.article.ArticleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("일반 Join 쿼리문은 N+1 문제가 발생한다")
    void noFetchQuery () {
        //given
        Member member1 = createMember();
        createArticle(member1);

        entityManager.flush();
        entityManager.clear();

        //when
        List<Member> members = memberRepository.findAllJPQL();
        for (Member member : members) {
            List<Article> articles = member.getArticles(); //쿼리 O
            System.out.println("articles = " + articles);
        }

        //articles 에 대해서 조회할 때, 쿼리가 한번더 일어난다.
    }

    @Test
    @DisplayName("Join Fetch 쿼리문은 한번만 조회된다")
    void fetchQuery () {
        //given
        Member member1 = createMember();
        createArticle(member1);

        entityManager.flush();
        entityManager.clear();

        //when
        List<Member> members = memberRepository.findAllJPQLFetch();
        for (Member member : members) {
            List<Article> articles = member.getArticles(); //쿼리 X
            System.out.println("articles = " + articles);
        }

        //articles 에 대해서 조회할 때, 쿼리가 일어나지 않는다.
    }



    private Member createMember() {
        Member member = Member.create("01012341234", "ABC");
        return memberRepository.create(member);
    }

    private void createArticle(Member member) {
        Article article = Article.create("123", "456", member);
        Article article2 = Article.create("123", "456", member);
        Article article3 = Article.create("123", "456", member);
        Article article4 = Article.create("123", "456", member);
        Article article5 = Article.create("123", "456", member);
        articleRepository.create(article);
        articleRepository.create(article2);
        articleRepository.create(article3);
        articleRepository.create(article4);
        articleRepository.create(article5);
    }

}