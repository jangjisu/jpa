package com.example.connect.api.domain.order;

import com.example.connect.api.domain.item.Book;
import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.orderitem.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderTest {

    @PersistenceUnit
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private Order order;
    private List<Item> initialItems = new ArrayList<>();
    private List<Item> newItems = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // JPA 엔티티 매니저 설정
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin(); // 트랜잭션 시작

        order = new Order();

        List<Book> collect = IntStream.range(1, 10)
                .mapToObj(i -> createAndPersistBook("Book" + i, "Author" + i, "ISBN" + i))
                .toList();

        initialItems.add(collect.get(0));
        initialItems.add(collect.get(1));
        initialItems.add(collect.get(2));
        initialItems.add(collect.get(3));


        for (Item item : initialItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        newItems.add(collect.get(2));
        newItems.add(collect.get(3));
        newItems.add(collect.get(4));
        newItems.add(collect.get(5));

        em.persist(order);
        em.flush(); // 변경 사항을 DB에 반영
        em.clear(); // 영속성 컨텍스트 초기화 (준영속 상태로 변경)
    }

    @Test
    @DisplayName("changeOrderBooks")
    void testChangeOrderItems() {

        order.changeOrderItems(newItems);
        em.flush(); // 여기서 실제 쿼리가 실행되며 오류 발생 예상

        assertThat(order.getOrderItems()).hasSize(4);
        assertThat(order.getOrderItems())
                .extracting("item.name")
                .containsExactly("Book3", "Book4", "Book5", "Book6"); // Book1 삭제, Book5 추가됨


        em.close();
    }

    private Book createAndPersistBook(String name, String author, String isbn) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setIsbn(isbn);

        em.persist(book);
        return book;
    }
}
