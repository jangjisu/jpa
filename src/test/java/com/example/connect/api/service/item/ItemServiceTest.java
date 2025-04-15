package com.example.connect.api.service.item;

import com.example.connect.api.domain.item.*;
import com.example.connect.api.domain.orderitem.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("상품등록 테스트")
    @Test
    void 상품등록() {
        // given
        Book book = Book.create("ORM", 5000, 3, "김영한", "?");

        // when
        Long savedId = itemService.saveItem(book);

        // then
        Long id = itemRepository.findById(savedId).get().getId();
        assertThat(savedId).isEqualTo(id);
    }

    @DisplayName("같은 트랜잭션 내가 아닐경우 같은 엔티티를 조회하더라도, 서로 메모리 주소는 다르다.")
    @Test
    void testEqual() {
        // given
        Book book = Book.create("ORM", 5000, 3, "김영한", "?");

        // when
        Item item = itemService.saveItemEntity(book);

        // then
        Item item1 = itemRepository.findById(item.getId()).get();
        //동일성 비교시 동일하지 않은 객체로 판단한다 (같은 트랜잭션 내 조회가 아님)
        assertThat(item1).isNotEqualTo(item);
    }

    @DisplayName("")
    @Test
    void 상속관계와_프록시_VisitorPattern() {
        // given
        OrderItem orderItem = OrderItem.create(createItem(), 2000, 0);
        Item item = orderItem.getItem();

        item.accept(new PrintVisitor());
        //movie.class = class com.example.connect.api.domain.item.Movie
    }

    private Item createItem() {
        Movie movie = new Movie();
        return itemRepository.save(movie);
    }
}