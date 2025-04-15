package com.example.connect.api.domain.user;

import com.example.connect.IntegrationJpaTestSupport;
import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.article.ArticleRepository;
import com.example.connect.api.domain.delivery.Delivery;
import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.item.Book;
import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.item.ItemRepository;
import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.MemberRepository;
import com.example.connect.api.domain.order.Order;
import com.example.connect.api.domain.order.OrderRepository;
import com.example.connect.api.domain.orderitem.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRepositoryTest extends IntegrationJpaTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        memberRepository.deleteAll();
        articleRepository.deleteAll();

        createUserArticleScenario(5, 5);

        entityManager.flush();
        entityManager.clear();
    }

    private Statistics getStatistics() {
        return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class).getStatistics();
    }

    @Test
    @DisplayName("일반 Join 쿼리문은 BatchSize 개수만큼 한번에 구해온다")
    void batchSizeQuery() {
        //when
        getStatistics().clear();
        List<User> users = userRepository.findAllJPQL();
        for (User user : users) {
            Set<Article> articles = user.getArticles(); //쿼리 O
            System.out.println("articles = " + articles);
        }

        final int EXPECTED_ARTICLE_BATCH_SIZE = 2;
        int queryCount = (int) Math.ceil((double) users.size() / EXPECTED_ARTICLE_BATCH_SIZE);
        //articles 에 대해서 조회할 때, 쿼리가 한번더 일어난다. 1 + N
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1 + queryCount);
    }

    @Test
    @DisplayName("두개이상 1:N 연관관계를 지닌 엔티티를 Set 형식으로 가져오기 가능하다.")
    void solveMultipleBagFetchException () {
        Member member = createMember();
        Book book = createBook();
        User user = createUser();
        Article article = createArticle();
        Order order = createOrder(member, book);

        user.addArticle(article);
        user.addOrder(order);

        List<User> result = userRepository.findAllWithTwoEntities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArticles()).hasSize(1);
        assertThat(result.get(0).getOrders()).hasSize(1);
    }



    private User createUser() {
        User user = User.create("01012345678", "DEF");
        return userRepository.save(user);
    }

    private Book createBook() {
        Book book = Book.create("ORM", 5000, 3, "김영한", "?");
        return itemRepository.save(book);
    }

    private Order createOrder(Member member, Item item) {
        Address address = Address.create("", "", "");
        OrderItem orderItem = OrderItem.create(item, item.getPrice(), 2);
        Order order = Order.createOrder(member, Delivery.create(address), orderItem);
        return orderRepository.save(order);
    }

    private Article createArticle() {
        Article article = Article.create("123", "456");
        return articleRepository.create(article);
    }

    private Member createMember() {
        Member member = Member.create("01012341234", "ABC");
        return memberRepository.create(member);
    }

    private void createUserArticleScenario(int userCount, int articlesPerMember) {
        for (int i = 0; i < userCount; i++) {
            User user = createUser();
            for (int j = 0; j < articlesPerMember; j++) {
                Article article = createArticle();
                article.setUser(user);
            }
        }
    }
}