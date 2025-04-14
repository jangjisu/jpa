package com.example.connect.api.domain.member;

import com.example.connect.IntegrationJpaTestSupport;
import com.example.connect.api.domain.article.Article;
import com.example.connect.api.domain.article.ArticleRepository;
import com.example.connect.api.domain.delivery.Delivery;
import com.example.connect.api.domain.embedded.Address;
import com.example.connect.api.domain.item.Book;
import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.item.ItemRepository;
import com.example.connect.api.domain.order.Order;
import com.example.connect.api.domain.order.OrderRepository;
import com.example.connect.api.domain.orderitem.OrderItem;
import com.example.connect.api.domain.user.User;
import com.example.connect.api.domain.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class MemberRepositoryTest extends IntegrationJpaTestSupport {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

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
    @DisplayName("일반 Join 쿼리문은 BatchSize 개수만큼 한번에 구해온다")
    void batchSizeQuery() {
        //멤버에 대한 조회를 막기 위해 미리 해온다..
        preloadMembersToCache();

        //when
        getStatistics().clear();
        List<User> users = userRepository.findAllJPQL();
        for (User user : users) {
            Set<Article> articles = user.getArticles(); //쿼리 O
            System.out.println("articles = " + articles);
        }

        int articleBatchSize = 2;
        int queryCount = (int) Math.ceil((double) users.size() / articleBatchSize);
        //articles 에 대해서 조회할 때, 쿼리가 한번더 일어난다. 1 + N
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1 + queryCount);
    }

    private void preloadMembersToCache() {
        List<Member> members = memberRepository.findAll();
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

    @Test
    @DisplayName("Fetch를 추가하지 않아도, EntityGraph를 사용하면 한번만 조회된다")
    void fetchQueryEntityGraph() {
        //when
        getStatistics().clear();
        List<Member> members = memberRepository.findAllJPQLEntityGraphFetch();
        for (Member member : members) {
            List<Article> articles = member.getArticles(); //쿼리 X
            System.out.println("articles = " + articles);
        }

        //articles 에 대해서 조회할 때, 쿼리가 일어나지 않는다.
        assertThat(getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Pageable과 함께 Fetch를 사용하면, 인메모리를 적용해서 조인 부분이 나온다")
    void fetchWithPagination() {
        //when
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<Member> members = memberRepository.findAllPage(pageRequest);

        for (Member member : members) {
            System.out.println(member.getArticles().size());
        }

        //WARN 7344 --- [my-jpa-application] [    Test worker] org.hibernate.orm.query                  : HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory 발생
    }

    @Test
    @DisplayName("두개이상 1:N 연관관계를 지닌 엔티티를 List 형식으로 가져올 경우 MultipleBagFetchException 를 감싼 InvalidDataAccessApiUsageException 이 발생한다.")
    void makeMultipleBagFetchException () {
        //MultipleBagFetchException 을 감싸고 있음..
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            memberRepository.findAllWithTwoEntities();
        });
    }

    @Test
    @DisplayName("두개이상 1:N 연관관계를 지닌 엔티티를 Set 형식으로 가져오기 가능하다.")
    void solveMultipleBagFetchException () {
        Member member = createMember();
        Book book = createBook();
        User user = createUser();
        Article article = createArticle(member);
        Order order = createOrder(member, book);

        user.addArticle(article);
        user.addOrder(order);

        List<User> result = userRepository.findAllWithTwoEntities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArticles()).hasSize(1);
        assertThat(result.get(0).getOrders()).hasSize(1);
    }

    private Member createMember() {
        Member member = Member.create("01012341234", "ABC");
        return memberRepository.create(member);
    }

    private Article createArticle(Member member) {
        Article article = Article.create("123", "456", member);
        return articleRepository.create(article);
    }

    private void createScenario(int memberCount, int articlesPerMember) {
        for (int i = 0; i < memberCount; i++) {
            Member member = createMember();
            User user = createUser();
            for (int j = 0; j < articlesPerMember; j++) {
                Article article = createArticle(member);
                article.setUser(user);
            }
        }
    }

    private Book createBook() {
        Book book = Book.create("ORM", 5000, 3, "김영한", "?");
        return itemRepository.save(book);
    }

    private User createUser() {
        User user = User.create("01012345678", "DEF");
        return userRepository.save(user);
    }

    private Order createOrder(Member member, Item item) {
        Address address = Address.create("", "", "");
        OrderItem orderItem = OrderItem.create(item, item.getPrice(), 2);
        Order order = Order.createOrder(member, Delivery.create(address), orderItem);
        return orderRepository.save(order);
    }

}