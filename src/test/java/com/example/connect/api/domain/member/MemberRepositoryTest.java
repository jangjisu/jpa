package com.example.connect.api.domain.member;

import com.example.connect.IntegrationJpaTestSupport;
import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.article.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberRepositoryTest extends IntegrationJpaTestSupport {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics getStatistics() {
        return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class).getStatistics();
    }

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        memberRepository.deleteAll();
        articleRepository.deleteAll();

        createScenario(5, 5);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("일반 Join 쿼리문은 N+1 문제가 발생한다")
    void noFetchQuery () {
        //when
        getStatistics().clear();
        List<Member> members = memberRepository.findAllJPQL();
        for (Member member : members) {
            List<Article> articles = member.getArticles(); //쿼리 O
            System.out.println("articles = " + articles);
        }
        //articles 에 대해서 조회할 때, 쿼리가 한번더 일어난다. 1 + N
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1 + members.size());
    }

    @Test
    @DisplayName("Join Fetch 쿼리문은 한번만 조회된다")
    void fetchQuery () {
        //when
        getStatistics().clear();
        List<Member> members = memberRepository.findAllJPQLFetch();
        for (Member member : members) {
            List<Article> articles = member.getArticles(); //쿼리 X
            System.out.println("articles = " + articles);
        }

        //articles 에 대해서 조회할 때, 쿼리가 일어나지 않는다.
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }



    private Member createMember() {
        Member member = Member.create("01012341234", "ABC");
        return memberRepository.create(member);
    }

    private void createArticle(Member member) {
        Article article = Article.create("123", "456", member);
        articleRepository.create(article);
    }

    private void createScenario(int memberCount, int articlesPerMember) {
        for (int i = 0; i < memberCount; i++) {
            Member member = createMember();
            for (int j = 0; j < articlesPerMember; j++) {
                createArticle(member);
            }
        }
    }

}