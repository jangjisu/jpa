package com.example.connect.api.service.item;

import com.example.connect.api.domain.item.Book;
import com.example.connect.api.domain.item.ItemRepository;
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
}